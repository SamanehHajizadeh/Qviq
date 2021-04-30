package com.springboot.qviq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.qviq.model.Info;
import com.springboot.qviq.repository.InfoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath*:sql/cleanup.sql")
public class MessageLogsController_IT {

    private static final ObjectMapper mapper = new ObjectMapper();

    private final long max_age = 1700000;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private InfoRepository infoRepository;

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath*:sql/logs.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath*:sql/cleanup.sql")
    public void getAllMessageAPI() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/all")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("/all").exists())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();
//        todo
//                .andDo(MockMvcResultHandlers.print());
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
        Info newMessage = Info.builder()
                .logId(1L)
                .name("Samane")
                .messageContent("Message1")
                .date(null)
                .build();

        mvc.perform(MockMvcRequestBuilders
                .post("/NewMessage")
                .content(om.writeValueAsString(newMessage))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
//                .andExpect((ResultMatcher) jsonPath("$.name", "Sisil"));
        // .andExpect(jsonPath("$.messageId", is(1)))
//                .andExpect(jsonPath("$.name", "Sisil")))
//                .andExpect(jsonPath("$.messageContent", is("ggg!")));

        final List<Info> infos = infoRepository.findAll();
        //assertions
        Mockito.verify(infoRepository, times(1)).save(any(Info.class));
    }

    @Test
    public void find_all_OK() throws Exception {
        List<Info> messages = Arrays.asList(
                Info.builder()
                        .logId(10L)
                        .name("A")
                        .messageContent("messageContent A ")
                        .date(new Date())
                        .build(),
                Info.builder()
                        .logId(20L)
                        .name("B")
                        .messageContent("messageContent B ")
                        .date(new Date())
                        .build());

        mvc.perform(get("/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].logId", is(10)))
                .andExpect(jsonPath("$[0].name", is("A")))
                .andExpect(jsonPath("$[0].messageContent", is("messageContent A ")))
                .andExpect(jsonPath("$[1].logId", is(20)))
                .andExpect(jsonPath("$[1].name", is("B")))
                .andExpect(jsonPath("$[1].messageContent", is("messageContent B ")));

        verify(infoRepository, times(1)).findAll();
    }

    @Test
    public void find_IdNotFound_404() throws Exception {
        mvc.perform(get("/getLog/5")).andExpect(status().isNotFound());
    }

    @Test
    public void save_OK() throws Exception {
        Info newM = Info.builder()
                .logId(10L)
                .name("A")
                .messageContent("messageContent A ")
                .date(new Date())
                .build();
        when(infoRepository.save(any(Info.class))).thenReturn(newM);

        mvc.perform(post("/NewMessage")
                .content(mapper.writeValueAsString(newM))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())//.andExpect(status().isCreated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.logId", is(1)))
                .andExpect(jsonPath("$.name", is("Spring Boot Guide")))
                .andExpect(jsonPath("$.messageContent", is("Spring Boot Guide For test")));

        verify(infoRepository, times(1)).save(any(Info.class));

    }


    @Test
    public void delete_OK() throws Exception {

        doNothing().when(infoRepository).deleteById(1L);

        mvc.perform(delete("/Message/1"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(infoRepository, times(1)).deleteById(1L);
    }

    // @Test
    public void patch_M_405() throws Exception {

        //If U add save it will return 200
        // when(infoRepository.save(any(Message.class))).thenReturn(new Message());
        String patchInJson = "{\"messageContent\":\"kkkkk\"}";

        mvc.perform(patch("/update/1")
                .content(patchInJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        verify(infoRepository, times(1)).findById(1L);
        verify(infoRepository, times(0)).save(any(Info.class));
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
