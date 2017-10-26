/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.BomberManX.controllers;

import edu.eci.arsw.BomberManX.services.BomberManXServices;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sergioxl
 */
@RestController
@RequestMapping("/users")
public class BomberManXAPIControllerUser {
    
    
    @Autowired
    BomberManXServices gameServices;
    
    @Autowired
    SimpMessagingTemplate msmt;
    
    @RequestMapping(method = RequestMethod.GET)
    public String test(){
        return "Ok";
    }
    
    private static final Logger LOG = Logger.getLogger(BomberManXAPIControllerUser.class.getName());
    
    @RequestMapping(path = "/{correo}/{clave}", method = RequestMethod.GET)   
    
    public ResponseEntity<?> loginJugador(@PathVariable String correo, @PathVariable String clave){       

        return new ResponseEntity<>(gameServices.loginJugador(correo, clave) ,HttpStatus.ACCEPTED);
        
    }   
   
}