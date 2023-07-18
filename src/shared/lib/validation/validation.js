const validateEmail = (email) => {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
  }

  const validatePassword = (password) => {
    if (password.length < 8) {
      return false;
    }
  
    if (!/\d/.test(password)) {
      return false;
    }
  
    if (!/[A-Z]/.test(password)) {
      return false;
    }
  
    if (!/[a-z]/.test(password)) {
      return false;
    }
  
    return true;
  }
  

  const isValidSymbol = (username) => {
    const regex = /^[a-zA-Z0-9_]+$/;
  
    return regex.test(username);
  }



  export const validate = { isValidSymbol, validateEmail, validatePassword };