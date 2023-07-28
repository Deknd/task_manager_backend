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
    const unsafeCharacters = /[\u0000-\u001f\u007f-\u009f<>\\='";{}()]/;

    return !unsafeCharacters.test(username);
  }
  const errorMessanger = (errorDataArray) => {

    const array = [];
    errorDataArray.forEach((errorData)=>{
      const   errorText =  errorData.errorText;
      const functionValidation = errorData.functionValidation;
      if(functionValidation){
        array[array.length]=errorText;
      }
    })
    if(array.length !== 0){
      if(array.length>1){
        const result = array.join(", ");
        return result;

      } else { return array; }
      
    }



  }



  export const validate = { isValidSymbol, validateEmail, validatePassword, errorMessanger };