package fr.jerome.springbootrestserverapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = {"fr.jerome.springbootrestserverapi"})
/*
Annotation à placer sur la classe qui doit être un controleur
Permet la gestion globale des exceptions dans un service REST
Permet d'intercepter et de gérer plusieurs types d'erreurs grâce à l'annotation ExceptionHandler

variable basePackage -> indiquer dans quels packages se trouvent les contrôleurs à prendre en compte par cette classe
 */
public class GlobalHandlerControllerException extends ResponseEntityExceptionHandler {
    @InitBinder
    /*
    Annotation permettant une initialisation globale
     */
    public void dataBinding(WebDataBinder binder){
        /*On peut intialiser toute donnée ici, cette classe avec son annotation permet un démarrage automatique
        au lancement de l'application*/
    }

    @ModelAttribute
    /*
    Permet de créer un objet model global
    La variable "technicalError" ci-dessous pourra être utilisée n'importe ou dans l'application
     */
    public void globalAttributes(Model model){
        model.addAttribute("technicalError", "Une erreur technique est survenue !");
    }

    @ExceptionHandler(TechnicalErrorException.class)
    public ModelAndView technicalErrorException(Exception exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception.getMessage());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(Exception.class)//toutes les autres erreurs non gérées sont interceptées ici
    public ResponseEntity<BusinessResourceExceptionResponse> unknowError(HttpServletRequest req, Exception ex) {
        BusinessResourceExceptionResponse response = new BusinessResourceExceptionResponse();
        response.setErrorCode("Technical Error");
        response.setErrorMessage(ex.getMessage());
        response.setRequestURL(req.getRequestURL().toString());
        return new ResponseEntity<BusinessResourceExceptionResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessResourceException.class)
    public ResponseEntity<BusinessResourceExceptionResponse> resourceNotFound(HttpServletRequest req, BusinessResourceException ex) {
        BusinessResourceExceptionResponse response = new BusinessResourceExceptionResponse();
        response.setStatus(ex.getStatus());
        response.setErrorCode(ex.getErrorCode());
        response.setErrorMessage(ex.getMessage());
        response.setRequestURL(req.getRequestURL().toString());
        return new ResponseEntity<BusinessResourceExceptionResponse>(response, ex.getStatus());
    }
}
