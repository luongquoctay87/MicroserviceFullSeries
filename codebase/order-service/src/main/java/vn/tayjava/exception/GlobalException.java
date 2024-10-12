package vn.tayjava.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestControllerAdvice
@Slf4j(topic = "GLOBAL-EXCEPTION")
public class GlobalException {

    /**
     * Handle request data
     *
     * @param e
     * @param request
     * @return error
     */
    @ExceptionHandler({ConstraintViolationException.class,
            MissingServletRequestParameterException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(BAD_REQUEST)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "400 Response",
                                    summary = "Handle request data",
                                    value = """
                                            {
                                              "timestamp": "2023-10-19T06:26:34.388+00:00",
                                              "status": 400,
                                              "path": "/order/add",
                                              "error": "Invalid request",
                                              "messages": "price is invalid"
                                            }"""
                            ))})
    })
    public Error handleValidationException(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);

        Error error = new Error();
        error.setTimestamp(new Date());
        error.setStatus(BAD_REQUEST.value());
        error.setPath(request.getDescription(false).replace("uri=", ""));

        String message = e.getMessage();
        if (e instanceof MethodArgumentNotValidException) {
            int start = message.lastIndexOf("[");
            int end = message.lastIndexOf("]");
            message = message.substring(start + 1, end - 1);
            error.setError("Invalid payload");
            error.setMessages(message);
        } else if (e instanceof MissingServletRequestParameterException) {
            error.setError("Invalid parameter");
            error.setMessages(message);
        } else if (e instanceof ConstraintViolationException) {
            error.setError("Invalid parameter");
            error.setMessages(message.substring(message.indexOf(" ") + 1));
        } else {
            error.setError("Invalid data");
            error.setMessages(message);
        }

        return error;
    }

    @Getter
    @Setter
    private static class Error {
        private Date timestamp;
        private int status;
        private String path;
        private String error;
        private String messages;
    }
}
