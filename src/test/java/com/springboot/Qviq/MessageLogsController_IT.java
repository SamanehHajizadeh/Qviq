package com.springboot.Qviq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.Qviq.model.Info;
import com.springboot.Qviq.repository.InfoRepository;
import com.springboot.Qviq.service.InfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MessageLogsController_IT
{
    private final long max_age =1700000;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private InfoRepository mock;


    @Test
    public void getAllMessageAPI() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .get("/all")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("/all").exists())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void save_Log_OK() throws Exception {
        ObjectMapper om = new ObjectMapper();

        String json = "{\n" +
                "\n" +
                "    \"name\": \"Sisil\",\n" +
                "    \"messageContent\" : \"Sisilia is testing!\"\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    ";
        Info newMessage = new Info(1L, "Samane", "Message1", null);
        when(mock.save(any(Info.class))).thenReturn(newMessage);

        mvc.perform( MockMvcRequestBuilders
                .post("/NewMessage")
                .content(om.writeValueAsString(newMessage))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
//                .andExpect((ResultMatcher) jsonPath("$.name", "Sisil"));
               // .andExpect(jsonPath("$.messageId", is(1)))
//                .andExpect(jsonPath("$.name", "Sisil")))
//                .andExpect(jsonPath("$.messageContent", is("ggg!")));

        Mockito.verify(mock, times(1)).save(any(Info.class));
    }

    //    @Test
    public void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Hello World!")));
    }

    //    @Test
    public void get_() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) CacheControl.maxAge(max_age, TimeUnit.SECONDS));
//                .andExpect(content().string(equalTo("Hello World!")));;
    }

//    @Test
    public void getDefault() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                        .header()
                        .string("Cache-Control", "no-cache, no-store, must-revalidate"));
    }
}
