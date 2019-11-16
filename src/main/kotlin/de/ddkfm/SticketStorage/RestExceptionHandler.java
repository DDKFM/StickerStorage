package de.ddkfm.SticketStorage;

import de.ddkfm.SticketStorage.models.LocationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.ResponseEntity.notFound;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    private static Logger log = LogManager.getLogger("SticketStorage");

    @ExceptionHandler(value = {LocationNotFoundException.class})
    public ResponseEntity vehicleNotFound(LocationNotFoundException ex, WebRequest request) {
        log.debug("handling LocationNotFoundException ...");
        return notFound().build();
    }
}	