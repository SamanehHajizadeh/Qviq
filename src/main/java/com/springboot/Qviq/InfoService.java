package com.springboot.Qviq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Date;
import java.util.List;


@Service
public class InfoService implements IInfoService {

    @Autowired
    private InfoRepository repository;

    @Override
    public Info addMessage(String name, int logId, String message){
        return repository
                .findById(Long.valueOf(logId))
                .map(x -> {
                            x.setMessageContent(message);
                            x.setName(name);
                            x.setDate(new Date());
                    return repository.save(x);
                })
                .get();

    }

    @Override
    public List<Info> findAll() {
        return repository.findAll();
    }

    @Override
    public Info getLog(int logId)  {
        if(repository.findById(Long.valueOf(logId)).isPresent() == false)
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "404"
            );
        return  repository.findById(Long.valueOf(logId))
                .orElseThrow(() -> new InfoNotFoundException(Long.valueOf(logId)));
    }


}
