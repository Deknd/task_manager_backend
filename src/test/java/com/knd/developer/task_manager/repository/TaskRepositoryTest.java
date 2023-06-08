package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.IntegrationTestBase;
import com.knd.developer.task_manager.domain.task.PriorityTask;
import com.knd.developer.task_manager.domain.task.Status;
import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryTest  extends IntegrationTestBase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
   private TaskRepository taskRepository;
   private UUID uuid = UUID.randomUUID();

    private String id = uuid.toString();
    private Long idLong = Math.abs(uuid.getMostSignificantBits());


    @Test
    void taskRepositoryTest_CreateAndFindById_Task(){
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
        Optional<User> resultUser = userRepository.findById(user.getId());

        //then
        assertFalse(resultUser.isEmpty());
        User returnUser = resultUser.get();
        List<Task> result = returnUser.getTasks();
        Task[] testTask= result.toArray(new Task[result.size()]);
        Task[] resultTask = new Task[testTask.length];
        for(int i = 0; i<testTask.length; i++){

           Optional<Task> optionalTask=taskRepository.findById(testTask[i].getId());
           assertFalse(optionalTask.isEmpty());
           resultTask[i]=optionalTask.get();

        }



        assertArrayEquals(testTask,resultTask);


        delete(user.getId());

    }
    @Test
    void taskRepositoryTest_FindByIdIfNull_Task(){
        Optional<Task> nullTask = taskRepository.findById(idLong);
        assertTrue(nullTask.isEmpty());

    }


    @Test
    void taskRepositoryTest_CreateAndFindByUserId_ListTask(){
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
        Optional<User> resultUser = userRepository.findById(user.getId());

        //then
        assertFalse(resultUser.isEmpty());
        User returnUser = resultUser.get();
        List<Task> result = returnUser.getTasks();
        List<Task> returnTask = taskRepository.findAllByUserId(user.getId());

        assertIterableEquals(result, returnTask);

        delete(user.getId());

    }

    @Test
    void taskRepositoryTest_FindByUserIdIfNull_ListTask(){
        List<Task>  nullTask = taskRepository.findAllByUserId(idLong);
        assertTrue(nullTask.isEmpty());
    }
    @Test
    void taskRepositoryTest_IsTaskOwner_UserIdAndId(){

        User user1 = new User();
        user1.setName("TestName");
        user1.setUsername("TestUserName1 " + id);
        user1.setPassword("TestPassword");

        User user2 = new User();
        user2.setName("TestName");
        user2.setUsername("TestUserName2 " + id);
        user2.setPassword("TestPassword");



        userRepository.create(user1);
        userRepository.create(user2);

        Task task1 = new Task();
        task1.setUser_id(user1.getId());
        task1.setTitle("Тест 1");
        task1.setDescription("Провожу тест по извлечению юезра");
        task1.setStatus(Status.TODO);
        task1.setPriority(PriorityTask.STANDARD);

        Task task2 = new Task();
        task2.setUser_id(user2.getId());
        task2.setTitle("Тест 2");
        task2.setDescription("Провожу второй тест");
        task2.setStatus(Status.TODO);
        task2.setPriority(PriorityTask.STANDARD);

        user1.setTasks(List.of(task1));
        user2.setTasks(List.of(task2));

        for (Task task4 : user1.getTasks()) {
            taskRepository.create(task4);
        }
        for (Task task4 : user2.getTasks()) {
            taskRepository.create(task4);
        }

        Optional<User> result1=userRepository.findById(user1.getId());
        Optional<User> result2=userRepository.findById(user2.getId());
        assertFalse(result1.isEmpty());
        assertFalse(result2.isEmpty());
        Task resultTask1=result1.get().getTasks().get(0);
        Task resultTask2=result2.get().getTasks().get(0);

        assertTrue(taskRepository.isTaskOwner(result1.get().getId(),resultTask1.getId()));
        assertTrue(taskRepository.isTaskOwner(result2.get().getId(),resultTask2.getId()));
        assertFalse(taskRepository.isTaskOwner(result1.get().getId(),resultTask2.getId()));
        assertFalse(taskRepository.isTaskOwner(result2.get().getId(),resultTask1.getId()));
        assertFalse(taskRepository.isTaskOwner(result1.get().getId(),null));
        assertFalse(taskRepository.isTaskOwner(null,resultTask2.getId()));


        delete(user1.getId());
        delete(user2.getId());



    }


    @Test
    void taskRepositoryTest_Update_Task(){

        User user1 = new User();
        user1.setName("TestName");
        user1.setUsername("TestUserName1 " + id);
        user1.setPassword("TestPassword");

        userRepository.create(user1);

        Task task1 = new Task();
        task1.setUser_id(user1.getId());
        task1.setTitle("Тест 1");
        task1.setDescription("Провожу тест по извлечению юезра");
        task1.setStatus(Status.TODO);
        task1.setPriority(PriorityTask.STANDARD);

        taskRepository.create(task1);

        Optional<User> result1=userRepository.findById(user1.getId());
        assertFalse(result1.isEmpty());
        Task resultTask1=result1.get().getTasks().get(0);

        resultTask1.setTitle("Тест Update");
        resultTask1.setDescription("Провожу тест по извлечению юезра Update");
        resultTask1.setStatus(Status.IN_PROGRESS);
        resultTask1.setPriority(PriorityTask.HIGH);

        taskRepository.update(resultTask1);

        Optional<Task> returnUpdate = taskRepository.findById(resultTask1.getId());

        assertFalse(returnUpdate.isEmpty());

        assertEquals(resultTask1.getTitle(),returnUpdate.get().getTitle());
        assertEquals(resultTask1.getDescription(),returnUpdate.get().getDescription());
        assertEquals(resultTask1.getStatus(),returnUpdate.get().getStatus());
        assertEquals(resultTask1.getPriority(),returnUpdate.get().getPriority());

        delete(user1.getId());

    }

    @Test
    void taskRepositoryTest_Delete_TaskId(){
        User user1 = new User();
        user1.setName("TestName");
        user1.setUsername("TestUserName1 " + id);
        user1.setPassword("TestPassword");

        userRepository.create(user1);

        Task task1 = new Task();
        task1.setUser_id(user1.getId());
        task1.setTitle("Тест 1");
        task1.setDescription("Провожу тест по извлечению юезра");
        task1.setStatus(Status.TODO);
        task1.setPriority(PriorityTask.STANDARD);

        taskRepository.create(task1);

        Optional<User> result1=userRepository.findById(user1.getId());
        assertFalse(result1.isEmpty());
        Task resultTask1=result1.get().getTasks().get(0);

        taskRepository.delete(resultTask1.getId());

        Optional<Task> task = taskRepository.findById(resultTask1.getId());
        assertTrue(task.isEmpty());

        delete(user1.getId());

    }







    private void delete(Long id) {


        userRepository.delete(id);
    }




}