package com.springboot.Qviq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.Qviq.model.Info;
import com.springboot.Qviq.repository.InfoRepository;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private InfoRepository mockRepository;


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
        when(mockRepository.save(any(Info.class))).thenReturn(newMessage);

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

        Mockito.verify(mockRepository, times(1)).save(any(Info.class));
    }

    @Test
    public void find_all_OK() throws Exception {

        List<Info> messages = Arrays.asList(
                new Info(10L, "A", "messageContent A ", new Date()),
                new Info(20L, "B", "messageContent B ", new Date()));

        when(mockRepository.findAll()).thenReturn(messages);

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

        verify(mockRepository, times(1)).findAll();
    }

    @Test
    public void find_IdNotFound_404() throws Exception {
        mvc.perform(get("/getLog/5")).andExpect(status().isNotFound());
    }

    @Test
    public void save_OK() throws Exception {
        Info newM = new Info(1L, "Spring Boot Guide", "Spring Boot Guide For test", null);
        when(mockRepository.save(any(Info.class))).thenReturn(newM);

        mvc.perform(post("/NewMessage")
                .content(mapper.writeValueAsString(newM))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())//.andExpect(status().isCreated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.logId", is(1)))
                .andExpect(jsonPath("$.name", is("Spring Boot Guide")))
                .andExpect(jsonPath("$.messageContent", is("Spring Boot Guide For test")));

        verify(mockRepository, times(1)).save(any(Info.class));

    }


    @Test
    public void delete_OK() throws Exception {

        doNothing().when(mockRepository).deleteById(1L);

        mvc.perform(delete("/Message/1"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockRepository, times(1)).deleteById(1L);
    }

    //    @Test
    public void patch_messageContent_OK() throws Exception {

        when(mockRepository.save(any(Info.class))).thenReturn(new Info());
        String patchInJson = "{\"messageContent\":\"update message Content\"}";

        mvc.perform(patch("/update/1")
                .content(patchInJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(1)).save(any(Info.class));

    }

    // @Test
    public void patch_M_405() throws Exception {

        //If U add save it will return 200
        // when(mockRepository.save(any(Message.class))).thenReturn(new Message());
        String patchInJson = "{\"messageContent\":\"kkkkk\"}";

        mvc.perform(patch("/update/1")
                .content(patchInJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(0)).save(any(Info.class));
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
