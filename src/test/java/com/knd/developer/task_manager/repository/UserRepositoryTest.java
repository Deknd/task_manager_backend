package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.domain.task.PriorityTask;
import com.knd.developer.task_manager.domain.task.Status;
import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.domain.user.User;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    UUID uuid = UUID.randomUUID();

    String id = uuid.toString();
    Long idLong = Math.abs(uuid.getMostSignificantBits());


    @Test
    void userRepositoryTest_Create_CreateUser() {
        //given
        User user = new User();

        user.setName("TestName");
        user.setUsername("TestUserName " + id);
        user.setPassword("TestPassword");
        user.setRoles(Set.of(Role.ROLE_USER));

        //when

        userRepository.create(user);
        Optional<User> resultUser = userRepository.findByUsername(user.getUsername());

        //then

        assertFalse(resultUser.isEmpty());
        assertNotNull(resultUser.get().getId());
        assertEquals(user.getName(), resultUser.get().getName());
        assertEquals(user.getUsername(), resultUser.get().getUsername());
        assertEquals(user.getPassword(), resultUser.get().getPassword());


            delete(resultUser.get().getId());

    }

    @Test
    void userRepositoryTest_Create_CreateUserRepeat() {
        User user = new User();

        user.setName("TestName");
        user.setUsername("TestUserName " + id);
        user.setPassword("TestPassword");
        user.setRoles(Set.of(Role.ROLE_USER));

        assertThrows(DuplicateKeyException.class, () -> {

            userRepository.create(user);
            userRepository.create(user);
        });

        Optional<User> resultUser = userRepository.findByUsername(user.getUsername());
        assertFalse(resultUser.isEmpty());
        delete(resultUser.get().getId());
    }

    @Test
    void userRepositoryTest_Create_CreateUserThereIsNoElement() {
        //given
        User userNoName = new User();
        userNoName.setName(null);
        userNoName.setUsername("TestUserName " + id);
        userNoName.setPassword("TestPassword");

        User userNoUsername = new User();
        userNoUsername.setName("TestName");
        userNoUsername.setUsername(null);
        userNoUsername.setPassword("TestPassword");

        User userNoPassword = new User();
        userNoPassword.setName("TestName");
        userNoPassword.setUsername("TestUserName " + id);
        userNoPassword.setPassword(null);

        User userEmpty = new User();

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.create(userNoName);
        });
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.create(userNoUsername);
        });
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.create(userNoPassword);
        });
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.create(userEmpty);
        });
    }

    @Test
    void userRepositoryTest_InsertUserRole_AddUserRole() {
        //given
        User user = new User();

        user.setName("TestName");
        user.setUsername("TestUserName " + id);
        user.setPassword("TestPassword");

        userRepository.create(user);
        Set<Role> roles = Set.of(Role.ROLE_USER, Role.ROLE_ADMIN);


        for (Role role : roles) {
            userRepository.insertUserRole(user.getId(), role.toString());

        }

        assertThrows(DataIntegrityViolationException.class, () -> {
            for (Role role : roles) {
                userRepository.insertUserRole(null, role.toString());

            }
        });

        assertThrows(DataIntegrityViolationException.class, () -> {

            userRepository.insertUserRole(user.getId(), null);


        });


        //when
        Optional<User> resultUser = userRepository.findById(user.getId());

        //then
        assertFalse(resultUser.isEmpty());

        assertEquals(resultUser.get().getRoles().size(), roles.size());

        delete(user.getId());

    }


    @Test
    void userRepositoryTest_FindById_IdAndNoIdAndNoUser() {
        //given
        User user = new User();

        user.setName("TestName");
        user.setUsername("TestUserName " + id);
        user.setPassword("TestPassword");
        user.setRoles(Set.of(Role.ROLE_USER, Role.ROLE_ADMIN));


        userRepository.create(user);

        Task task = new Task();
        task.setUser_id(user.getId());
        task.setTitle("Тест 1");
        task.setDescription("Провожу тест по извлечению юезра");
        task.setStatus(Status.TODO);
        task.setPriority(PriorityTask.STANDARD);

        Task task1 = new Task();
        task1.setUser_id(user.getId());
        task1.setTitle("Тест 2");
        task1.setDescription("Провожу второй тест");
        task1.setStatus(Status.TODO);
        task1.setPriority(PriorityTask.STANDARD);

        Task task2 = new Task();
        task2.setUser_id(user.getId());
        task2.setTitle("Тест 3");
        task2.setDescription("Провожу третий тест");
        task2.setStatus(Status.TODO);
        task2.setPriority(PriorityTask.STANDARD);

        Task task3 = new Task();
        task3.setUser_id(user.getId());
        task3.setTitle("Тест 4");
        task3.setDescription("Провожу четвертый тест");
        task3.setStatus(Status.TODO);
        task3.setPriority(PriorityTask.STANDARD);

        List<Task> tasks = List.of(task, task1, task2, task3);


        user.setTasks(tasks);


        for (Task task4 : user.getTasks()) {
            taskRepository.create(task4);
        }

        for (Role role : user.getRoles()) {
            userRepository.insertUserRole(user.getId(), role.toString());

        }
        //when
        Optional<User> resultNull = userRepository.findById(null);
        Optional<User> resultNoUser = userRepository.findById(idLong);

        Optional<User> resultUser = userRepository.findById(user.getId());

        assertFalse(resultUser.isEmpty());
        User result = resultUser.get();
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertSetEquals(user.getRoles(), result.getRoles());
        assertIterableEquals(user.getTasks(), result.getTasks());
        assertTrue(resultNull.isEmpty());
        assertTrue(resultNoUser.isEmpty());


        delete(user.getId());


    }
    @Test
    void userRepositoryTest_FindByUsername_UsernameAndNoUsernameAndNoUser() {
        //given
        User user = new User();

        user.setName("TestName");
        user.setUsername("TestUserName " + id);
        user.setPassword("TestPassword");


        userRepository.create(user);
        user.setRoles(Set.of(Role.ROLE_USER, Role.ROLE_ADMIN));

        Task task = new Task();
        task.setUser_id(user.getId());
        task.setTitle("Тест 1");
        task.setDescription("Провожу тест по извлечению юезра");
        task.setStatus(Status.TODO);
        task.setPriority(PriorityTask.STANDARD);

        Task task1 = new Task();
        task1.setUser_id(user.getId());
        task1.setTitle("Тест 2");
        task1.setDescription("Провожу второй тест");
        task1.setStatus(Status.TODO);
        task1.setPriority(PriorityTask.STANDARD);

        Task task2 = new Task();
        task2.setUser_id(user.getId());
        task2.setTitle("Тест 3");
        task2.setDescription("Провожу третий тест");
        task2.setStatus(Status.TODO);
        task2.setPriority(PriorityTask.STANDARD);

        Task task3 = new Task();
        task3.setUser_id(user.getId());
        task3.setTitle("Тест 4");
        task3.setDescription("Провожу четвертый тест");
        task3.setStatus(Status.TODO);
        task3.setPriority(PriorityTask.STANDARD);

        List<Task> tasks = List.of(task, task1, task2, task3);


        user.setTasks(tasks);


        for (Task task4 : user.getTasks()) {
            taskRepository.create(task4);
        }

        for (Role role : user.getRoles()) {
            userRepository.insertUserRole(user.getId(), role.toString());

        }
        //when
        Optional<User> resultNull = userRepository.findByUsername(null);
        Optional<User> resultNoUser = userRepository.findByUsername(id);

        Optional<User> resultUser = userRepository.findByUsername(user.getUsername());

        assertFalse(resultUser.isEmpty());
        User result = resultUser.get();
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertSetEquals(user.getRoles(), result.getRoles());
        assertIterableEquals(user.getTasks(), result.getTasks());
        assertTrue(resultNull.isEmpty());
        assertTrue(resultNoUser.isEmpty());


        delete(user.getId());


    }



    @Test
    void userRepositoryTest_Update_UpdateUser(){
        User user = new User();

        user.setName("TestName");
        user.setUsername("TestUserName " + id);
        user.setPassword("TestPassword");



        userRepository.create(user);

        user.setName("UpdateTestName");
        user.setUsername("UpdateTestUserName " + id);
        user.setPassword("UpdateTestPassword");

        userRepository.update(user);

        Optional<User> resultUser = userRepository.findById(user.getId());


        assertFalse(resultUser.isEmpty());
        User result = resultUser.get();
        assertEquals(user.getId(),result.getId());
        assertEquals(user.getName(),result.getName());
        assertEquals(user.getUsername(),result.getUsername());
        assertEquals(user.getPassword(),result.getPassword());


        delete(user.getId());

    }
    @Test
    void userRepositoryTest_Delete_DeleteUser() {
        //given
        User user = new User();

        user.setName("TestName");
        user.setUsername("TestUserName " + id);
        user.setPassword("TestPassword");

        //when

        userRepository.create(user);
        Optional<User> resultUser = userRepository.findByUsername(user.getUsername());

        //then

        assertFalse(resultUser.isEmpty());

        userRepository.delete(resultUser.get().getId());

        Optional<User> nullUser = userRepository.findByUsername(user.getUsername());

        assertTrue(nullUser.isEmpty());





    }




    private <T> void assertSetEquals(@NotNull Set<T> expected, @NotNull Set<T> actual) {
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }


    private void delete(Long id) {


        userRepository.delete(id);
    }


}