package com.quizapp.quizappapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

@RestController
public class QuizAppController {
    private static final Logger logger = LoggerFactory.getLogger(QuizAppController.class);

    @RequestMapping("/**")
    public ResponseEntity<?> handle(HttpServletRequest request){
        try{
            String uri = request.getRequestURI();
            String api = uri.substring(uri.indexOf("quizapp") + 8).replaceAll("/", "-");
            StringBuilder sb = new StringBuilder();
            sb.append(request.getMethod().toLowerCase());
            sb.append('-');
            //sb.append(api.substring(4));
            sb.append(api);
            String jsonFile = sb.toString();
            //Integer statusCode = Integer.valueOf(api.substring(0,3));
            String fileName = "json-dumps/" + jsonFile + ".json";
            logger.info("Filename detected = " + fileName);
            InputStream is = null;
            ClassPathResource cr = new ClassPathResource(fileName);
            if(cr.exists()){
                logger.info("Found file = " + fileName + ", in class path");
                is = cr.getInputStream();
            }

            if(is != null){
                Object o = new ObjectMapper().readValue(is, Object.class);
                return ResponseEntity.status(HttpStatus.valueOf(200)).body(o);
            }

        }catch (Exception e) {
            logger.error("Exception occurred in handle(): ", e);
        }
        return ResponseEntity.notFound().build();
    }
}
