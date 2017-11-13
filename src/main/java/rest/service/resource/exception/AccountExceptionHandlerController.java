package rest.service.resource.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import rest.service.exception.AccountExistsException;
import rest.service.exception.AccountNotFoundException;

@EnableWebMvc
@ControllerAdvice
public class AccountExceptionHandlerController {
    
    private static enum ErrorCode {
        DUPLICATE_ACCOUNT,
        ACCOUNT_NOT_FOUND,
        INTERNAL_ERROR
    }
    
    protected static class Error {
        private String code;
        private String info;
        
        public Error(
            String code, 
            String info) {
            
            this.code = code;
            this.info = info;
        }
        
        public String getCode() {
            return this.code;
        }
        
        public String getInfo() {
            return this.info;
        }
    }
    
    protected static class Result {
        private Error error;
        
        public Result(Error error) {
            this.error = error;
        }
        
        public Error getError() {
            return this.error;
        }
        
        public void setError(Error error) {
            this.error = error;
        }
    }
    
    private ModelAndView createModelView() {
        return new ModelAndView(new MappingJacksonJsonView());
    }
    
    private ModelAndView createError(
        String code, 
        String message) {
        
        ModelAndView mav = this.createModelView();
        
        mav.addObject(
            "result", 
            new Result(
                new Error(
                    code, 
                    message)));
        
        return mav;
    }
    
    @ExceptionHandler(AccountExistsException.class)
    public ModelAndView accountExistsHandleError( 
        AccountExistsException ex) {
        
        return this.createError(
            ErrorCode.DUPLICATE_ACCOUNT.name(), 
            ex.getMessage());
    }
    
    @ExceptionHandler(AccountNotFoundException.class)
    public ModelAndView accountNotFoundHandleError( 
        AccountNotFoundException ex) {
        
        return this.createError(
            ErrorCode.ACCOUNT_NOT_FOUND.name(), 
            null);
    }
    
    @ExceptionHandler(Throwable.class)
    public ModelAndView handleError( 
        Throwable ex) {
        
        return this.createError(
            ErrorCode.INTERNAL_ERROR.name(), 
            null);
    }
}
