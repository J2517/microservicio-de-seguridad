package com.mc.security.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mc.security.Models.Session;
import com.mc.security.Models.User;
import com.mc.security.Repositories.SessionRepository;
import com.mc.security.Repositories.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/sessions")

public class SessionController {
    @Autowired
    private SessionRepository theSessionRepository;

    @Autowired
    private UserRepository theUserRepository;

    @GetMapping("")
    public List<Session> find(){
        return this.theSessionRepository.findAll();
    }
    @GetMapping("{id}")
    public Session findById(@PathVariable String id){
        Session theSession=this.theSessionRepository.findById(id).orElse(null);
        return theSession;
    }
    @PostMapping
    public Session create(@RequestBody Session newSession){
        return this.theSessionRepository.save(newSession);
    }
    @PutMapping("{id}")
    public Session update(@PathVariable String id, @RequestBody Session newSession){
        Session actualSession=this.theSessionRepository.findById(id).orElse(null);
        if(actualSession!=null){
            actualSession.setToken(newSession.getToken());
            return actualSession;
        }else{
            return null;
        }
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Session theSession=this.theSessionRepository.findById(id).orElse(null);
        if (theSession!=null){
            this.theSessionRepository.delete(theSession);
        }
    }

    @PostMapping("{session_id}/user/{user_id}")
    public Session matchUser(@PathVariable String session_id,
                             @PathVariable String user_id){
        Session theSession=this.theSessionRepository.findById(session_id).orElse(null);
        User theUser=this.theUserRepository.findById(user_id).orElse(null);
        if (theSession!=null && theUser!=null){
            theSession.setUser(theUser);
            this.theSessionRepository.save(theSession);
            return theSession;
        } else{
            return null;
        }

    }

    @GetMapping("/user/{userId}")
    public List<Session> findByUser(@PathVariable String userId){
        return this.theSessionRepository.getSessionsByUser(userId);
    }
}

