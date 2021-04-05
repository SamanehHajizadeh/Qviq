package com.springboot.Qviq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpHeaders;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration
public class MessageControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;



    @MockBean
    private InfoRepository mockRepository;

    @Test
    public void find_all_OK() throws Exception {

        List<Info> messages = Arrays.asList(
                new Info(10L, "A", "messageContent A ", new Date()),
                new Info(20L, "B", "messageContent B ", new Date()));

        when(mockRepository.findAll()).thenReturn(messages);

        mockMvc.perform(get("/all"))
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
        mockMvc.perform(get("/getLog/5")).andExpect(status().isNotFound());
    }

    @Test
    public void save_OK() throws Exception {

        Info newM = new Info(1L, "Spring Boot Guide", "Spring Boot Guide For test", null);
        when(mockRepository.save(any(Info.class))).thenReturn(newM);

        mockMvc.perform(post("/NewMessage")
                .content(om.writeValueAsString(newM))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                /*.andDo(print())*/
//                .andExpect(status().isCreated())
                .andExpect(status().isOk())
                //  .andExpect(jsonPath("$.messageId", is(1)))
                .andExpect(jsonPath("$.name", is("Spring Boot Guide")))
                .andExpect(jsonPath("$.messageContent", is("Spring Boot Guide For test")));

        verify(mockRepository, times(1)).save(any(Info.class));

    }



//    @Test
//    public void patch_messageContent_OK() throws Exception {
//
//        when(mockRepository.save(any(Info.class))).thenReturn(new Info());
//        String patchInJson = "{\"messageContent\":\"update message Content\"}";
//
//        mockMvc.perform(patch("/update/1")
//                .content(patchInJson)
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        verify(mockRepository, times(1)).findById(1L);
//        verify(mockRepository, times(1)).save(any(Info.class));
//
//    }
//
   // @Test
    public void patch_M_405() throws Exception {

        //If U add save it will return 200
       // when(mockRepository.save(any(Message.class))).thenReturn(new Message());
        String patchInJson = "{\"messageContent\":\"kkkkk\"}";

        mockMvc.perform(patch("/update/1")
                .content(patchInJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(0)).save(any(Info.class));
    }
//
//    @Test
//    public void delete_OK() throws Exception {
//
//        doNothing().when(mockRepository).deleteById(1L);
//
//        mockMvc.perform(delete("/Message/1"))
//                /*.andDo(print())*/
//                .andExpect(status().isOk());
//
//        verify(mockRepository, times(1)).deleteById(1L);
//    }
//
//    private static void printJSON(Object object) {
//        String result;
//        try {
//            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
//            System.out.println(result);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//    }

}
