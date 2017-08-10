package ua.rd.cm.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.rd.cm.config.TestSecurityConfig;
import ua.rd.cm.config.WebMvcConfig;
import ua.rd.cm.config.WebTestConfig;
import ua.rd.cm.domain.*;
import ua.rd.cm.dto.MessageDto;
import ua.rd.cm.services.TalkService;
import ua.rd.cm.services.UserInfoService;
import ua.rd.cm.services.UserService;
import ua.rd.cm.dto.TalkDto;

import javax.servlet.Filter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebTestConfig.class, WebMvcConfig.class, TestSecurityConfig.class})
@WebAppConfiguration
public class TalkControllerTest extends TestUtil {
    private static final String API_TALK = "/api/talk";
    private static final String API_GET_USER_BY_ID = "/api/talk/1";
    private static final String SPEAKER_EMAIL = "ivanova@gmail.com";
    private static final String ORGANISER_EMAIL = "trybel@gmail.com";
    public static final String APPROVED = "Approved";
    public static final String IN_PROGRESS = "In Progress";
    public static final String REJECTED = "Rejected";
    public static final String NEW = "New";
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private TalkService talkService;
    @Autowired
    private TalkController talkController;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserService userService;

    private MockMvc mockMvc;

    private User speakerUser;
    private User organiserUser;
    private UserInfo userInfo;

    private Talk talk;
    private TalkDto correctTalkDto;

    MockHttpServletRequestBuilder requestBuilder;

    @Before
    public void setUp() {
        correctTalkDto = setupCorrectTalkDto();
        requestBuilder = MockMvcRequestBuilders.fileUpload(API_TALK).
                param("title", "title name").
                param("description", "desc").
                param("topic", "topic").
                param("type", "type").
                param("lang", "English").
                param("level", "Beginner");

        userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setShortBio("bio");
        userInfo.setJobTitle("job");
        userInfo.setPastConference("pastConference");
        userInfo.setCompany("EPAM");
        userInfo.setAdditionalInfo("addInfo");

        Set<Role> speakerRole = new HashSet<>();
        speakerRole.add(new Role(2L, Role.SPEAKER));
        speakerUser = new User();
        speakerUser.setId(1L);
        speakerUser.setFirstName("Olya");
        speakerUser.setLastName("Ivanova");
        speakerUser.setEmail("ivanova@gmail.com");
        speakerUser.setPassword("123456");
        speakerUser.setStatus(User.UserStatus.CONFIRMED);
        speakerUser.setUserInfo(userInfo);
        speakerUser.setUserRoles(speakerRole);

        Set<Role> organiserRole = new HashSet<>();
        organiserRole.add(new Role(1L, Role.ORGANISER));
        organiserUser = new User();
        organiserUser.setId(1L);
        organiserUser.setFirstName("Artem");
        organiserUser.setLastName("Trybel");
        organiserUser.setEmail("trybel@gmail.com");
        organiserUser.setPassword("123456");
        organiserUser.setStatus(User.UserStatus.CONFIRMED);
        organiserUser.setUserInfo(userInfo);
        organiserUser.setUserRoles(organiserRole);

        talk = createTalk(speakerUser);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(springSecurityFilterChain)
                .apply(springSecurity())
                .build();
        when(userService.getByEmail(eq(SPEAKER_EMAIL))).thenReturn(speakerUser);
        when(userService.getByEmail(eq(ORGANISER_EMAIL))).thenReturn(organiserUser);
        when(userInfoService.find(anyLong())).thenReturn(userInfo);
    }

    @After
    public void resetMocks() {
        Mockito.reset(talkService, userService);
    }

    @Test
    @Ignore
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void correctSubmitNewTalkTest() throws Exception {
        mockMvc.perform(requestBuilder)//preparePostRequest(API_TALK))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void emptyCompanyMyInfoSubmitNewTalkTest() throws Exception {
        userInfo.setCompany("");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void emptyJobMyInfoSubmitNewTalkTest() throws Exception {
        userInfo.setJobTitle("");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void emptyBioMyInfoSubmitNewTalkTest() throws Exception {
        userInfo.setShortBio("");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    @Test
    public void nullPrincipleSubmitNewTalkTest() throws Exception {
        expectUnauthorized(mockMvc.perform(requestBuilder));
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void nullTitleSubmitNewTalkTest() throws Exception {
        correctTalkDto.setTitle(null);

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void emptyTitleSubmitNewTalkTest() throws Exception {
        correctTalkDto.setTitle("");

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void tooLongTitleSubmitNewTalkTest() throws Exception {
        correctTalkDto.setTitle(createStringWithLength(251));

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void nullDescriptionSubmitNewTalkTest() throws Exception {
        correctTalkDto.setDescription(null);

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void emptyDescriptionSubmitNewTalkTest() throws Exception {
        correctTalkDto.setDescription("");

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void tooLongDescriptionSubmitNewTalkTest() throws Exception {
        correctTalkDto.setDescription(createStringWithLength(3001));

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void nullTopicSubmitNewTalkTest() throws Exception {
        correctTalkDto.setTopicName(null);

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void emptyTopicSubmitNewTalkTest() throws Exception {
        correctTalkDto.setTopicName("");

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void tooLongTopicSubmitNewTalkTest() throws Exception {
        correctTalkDto.setTopicName(createStringWithLength(256));

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void nullTypeSubmitNewTalkTest() throws Exception {
        correctTalkDto.setTypeName(null);

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void emptyTypeSubmitNewTalkTest() throws Exception {
        correctTalkDto.setTypeName("");

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void tooLongTypeSubmitNewTalkTest() throws Exception {
        correctTalkDto.setTypeName(createStringWithLength(256));

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void nullLanguageSubmitNewTalkTest() throws Exception {
        correctTalkDto.setLanguageName(null);

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void emptyLanguageSubmitNewTalkTest() throws Exception {
        correctTalkDto.setLanguageName("");

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void tooLongLanguageSubmitNewTalkTest() throws Exception {
        correctTalkDto.setLanguageName(createStringWithLength(256));

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void nullLevelSubmitNewTalkTest() throws Exception {
        correctTalkDto.setLevelName(null);

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void emptyLevelSubmitNewTalkTest() throws Exception {
        correctTalkDto.setLevelName("");

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void tooLongLevelSubmitNewTalkTest() throws Exception {
        correctTalkDto.setLevelName(createStringWithLength(256));

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void tooLongAddInfoSubmitNewTalkTest() throws Exception {
        correctTalkDto.setAdditionalInfo(createStringWithLength(1501));

        mockMvc.perform(preparePostRequest(API_TALK))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
//    public void correctGetMyTalksTest() throws Exception {
//        Talk talk = createTalk(speakerUser, organiserUser);
//        List<Talk> talks = new ArrayList<>();
//        talks.add(talk);
//
//        when(talkService.findByUserId(anyLong())).thenReturn(talks);
//
//        mockMvc.perform(prepareGetRequest(API_TALK))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].title", is(talk.getTitle())))
//                .andExpect(jsonPath("$[0].description", is(talk.getDescription())))
//                .andExpect(jsonPath("$[0].topic", is(talk.getTopic().getName())))
//                .andExpect(jsonPath("$[0].type", is(talk.getType().getName())))
//                .andExpect(jsonPath("$[0].lang", is(talk.getLanguage().getName())))
//                .andExpect(jsonPath("$[0].level", is(talk.getLevel().getName())))
//                .andExpect(jsonPath("$[0].addon", is(talk.getAdditionalInfo())))
//                .andExpect(jsonPath("$[0].status", is(talk.getStatus().getName())))
//                .andExpect(jsonPath("$[0].assignee", is(talk.getOrganiser().getFullName())));
//    }

    @Test
    public void incorrectGetMyTalksTest() throws Exception {
        Talk talk = createTalk(speakerUser);
        List<Talk> talks = new ArrayList<>();
        talks.add(talk);

        when(talkService.findByUserId(anyLong())).thenReturn(talks);

        expectUnauthorized(mockMvc.perform(prepareGetRequest(API_TALK)));
    }


//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void checkCallToPrepareOrganiserTalkMethod() throws Exception {
//        Talk talk = createTalk(speakerUser);
//        List talks = new ArrayList() {{
//            add(talk);
//            add(talk);
//        }};
//        when(talkService.findAll()).thenReturn(talks);
//
//        mockMvc.perform(prepareGetRequest(API_TALK))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)));
//    }

//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void correctGetAllTalks() throws Exception {
//        Talk talk = createTalk(speakerUser, organiserUser);
//        List<Talk> talks = new ArrayList<>();
//        talks.add(talk);
//
//        when(userService.getByEmail(eq(ORGANISER_EMAIL))).thenReturn(organiserUser);
//        when(talkService.findAll()).thenReturn(talks);
//        mockMvc.perform(prepareGetRequest(API_TALK))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].title", is(talk.getTitle())))
//                .andExpect(jsonPath("$[0].speakerId", is(Integer.parseInt(talk.getUser().getId().toString()))))
//                .andExpect(jsonPath("$[0].name", is(talk.getUser().getFirstName() + " " + talk.getUser().getLastName())))
//                .andExpect(jsonPath("$[0].description", is(talk.getDescription())))
//                .andExpect(jsonPath("$[0].topic", is(talk.getTopic().getName())))
//                .andExpect(jsonPath("$[0].type", is(talk.getType().getName())))
//                .andExpect(jsonPath("$[0].lang", is(talk.getLanguage().getName())))
//                .andExpect(jsonPath("$[0].level", is(talk.getLevel().getName())))
//                .andExpect(jsonPath("$[0].addon", is(talk.getAdditionalInfo())))
//                .andExpect(jsonPath("$[0].status", is(talk.getStatus().getName())))
//                .andExpect(jsonPath("$[0].date", is(talk.getTime().toString())))
//                .andExpect(jsonPath("$[0].comment", is(talk.getOrganiserComment())))
//                .andExpect(jsonPath("$[0].assignee", is(talk.getOrganiser().getFullName())));
//    }

//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void changeTalkStatusForNonExistingTalk() throws Exception {
//        when(talkService.findTalkById(anyLong())).thenThrow(new TalkNotFoundException());
//        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 0, "comment", "some state"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("error", is(TalkController.TALK_NOT_FOUND)));
//    }

    @Test
    public void unauthorizedChangeTalkStatus() throws Exception {
        expectUnauthorized(performTalkStatusChange(""));
    }

    private ResultActions performTalkStatusChange(String newState) throws Exception {
        return mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1L, "comment", newState));
    }

//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void organiserIsSetOnTalkReject() throws Exception {
//        Talk talk = createTalk(speakerUser, organiserUser);
//        when(talkService.findTalkById(anyLong())).thenReturn(talk);
//        testOrganiserIsSetOnStatusChange(REJECTED);
//    }

//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void organiserIsSetOnTalkInProgress() throws Exception {
//        Talk talk = createTalk(speakerUser, organiserUser);
//        when(talkService.findTalkById(anyLong())).thenReturn(talk);
//        testOrganiserIsSetOnStatusChange(IN_PROGRESS);
//    }
//
//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void organiserIsSetOnTalkApprove() throws Exception {
//        Talk talk = createTalk(speakerUser, organiserUser);
//        when(talkService.findTalkById(anyLong())).thenReturn(talk);
//        testOrganiserIsSetOnStatusChange(APPROVED);
//    }


    private void testOrganiserIsSetOnStatusChange(String newState) throws Exception {
        Talk talk = createTalk(speakerUser, organiserUser);
        when(talkService.findTalkById(anyLong())).thenReturn(talk);
        performTalkStatusChange(newState);
        verify(talkService, atLeastOnce()).update(eq(talk));
    }

//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void getTalkById() throws Exception {
//        Talk talk = createTalk(createUser());
//        when(talkService.findTalkById(anyLong())).thenReturn(talk);
//
//        expectTalk(mockMvc.perform(prepareGetRequest(API_TALK + "/" + 1)), talk)
//                .andExpect(jsonPath("assignee", is(nullValue())));
//    }
//
//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void getTalkByIdWithAssignee() throws Exception {
//        Talk talk = createTalk(createUser(), organiserUser);
//        when(talkService.findTalkById(anyLong())).thenReturn(talk);
//
//        expectTalk(mockMvc.perform(prepareGetRequest(API_TALK + "/" + 1)), talk)
//                .andExpect(jsonPath("assignee", is(talk.getOrganiser().getFullName())));
//    }

    private ResultActions expectTalk(ResultActions ra, Talk talk) throws Exception {
        return ra.andExpect(status().isOk())
                .andExpect(jsonPath("id", is(Integer.parseInt(talk.getId().toString()))))
                .andExpect(jsonPath("title", is(talk.getTitle())))
                .andExpect(jsonPath("speakerId", is(Integer.parseInt(talk.getUser().getId().toString()))))
                .andExpect(jsonPath("name", is(talk.getUser().getFullName())))
                .andExpect(jsonPath("description", is(talk.getDescription())))
                .andExpect(jsonPath("topic", is(talk.getTopic().getName())))
                .andExpect(jsonPath("type", is(talk.getType().getName())))
                .andExpect(jsonPath("lang", is(talk.getLanguage().getName())))
                .andExpect(jsonPath("level", is(talk.getLevel().getName())))
                .andExpect(jsonPath("addon", is(talk.getAdditionalInfo())))
                .andExpect(jsonPath("status", is(talk.getStatus().getName())))
                .andExpect(jsonPath("date", is(talk.getTime().toString())))
                .andExpect(jsonPath("comment", is(talk.getOrganiserComment())));
    }

    @Test
    public void incorrectGetTalkById() throws Exception {
        Talk talk = createTalk(createUser());
        when(talkService.findTalkById(anyLong())).thenReturn(talk);
        expectUnauthorized(mockMvc.perform(prepareGetRequest(API_TALK + "/" + 1)));
    }

//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void notFoundTalkById() throws Exception {
//        when(talkService.findTalkById(anyLong())).thenThrow(new TalkNotFoundException());
//        mockMvc.perform(prepareGetRequest(API_TALK + "/" + 1)).
//                andExpect(status().isNotFound());
//    }

    @Test
    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
    public void correctApproveNewTalk() throws Exception {
        when(talkService.findTalkById(anyLong())).thenReturn(createTalk(new User()));
        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1, "comment", APPROVED))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
    public void correctRejectNewTalk() throws Exception {
        when(talkService.findTalkById(anyLong())).thenReturn(createTalk(new User()));
        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1, "comment", REJECTED))
                .andExpect(status().isOk());
    }
//
//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void emptyCommentRejectNewTalk() throws Exception {
//        when(talkService.findTalkById(anyLong())).thenReturn(createTalk(new User()));
//        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1, "", REJECTED))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
    public void correctInProgressNewTalk() throws Exception {
        when(talkService.findTalkById(anyLong())).thenReturn(createTalk(new User()));
        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1, "comment", IN_PROGRESS))
                .andExpect(status().isOk());
    }

    @Test
    public void unauthorizedTalk() throws Exception {
        when(talkService.findTalkById(anyLong())).thenReturn(createTalk(new User()));
        expectUnauthorized(mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1, "comment", IN_PROGRESS)));
    }

//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void CommentToLong() throws Exception {
//        when(talkService.findTalkById(anyLong())).thenReturn(createTalk(new User()));
//        char[] array = new char[1001];
//        String tooLongComment = new String(array);
//        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1, tooLongComment, IN_PROGRESS))
//                .andExpect(status().isPayloadTooLarge());
//    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void emptyAdditionalInfoOnSpeakerTalkUpdate() throws Exception {
        User user = new User();
        user.setId(1L);
        when(talkService.findTalkById(anyLong())).thenReturn(createTalk(user));
        String empty = "";

        TalkDto talkDto = setupCorrectTalkDto();
        talkDto.setAdditionalInfo(empty);
        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1, talkDto))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void maxLengthAdditionalInfoOnSpeakerTalkUpdate() throws Exception {
        User user = new User();
        user.setId(1L);
        when(talkService.findTalkById(anyLong())).thenReturn(createTalk(user));
        char[] array = new char[1500];
        String maxLength = new String(array);

        TalkDto talkDto = setupCorrectTalkDto();
        talkDto.setAdditionalInfo(maxLength);
        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1, talkDto))
                .andExpect(status().isOk());
    }

//    @Test
//    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
//    public void additionalInfoTooLongOnSpeakerTalkUpdate() throws Exception {
//        when(talkService.findTalkById(anyLong())).thenReturn(createTalk(new User()));
//        char[] array = new char[1501];
//        String tooLongAdditionalInfo = new String(array);
//
//        TalkDto talkDto = setupCorrectTalkDto();
//        talkDto.setAdditionalInfo(tooLongAdditionalInfo);
//        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1, talkDto))
//                .andExpect(status().isPayloadTooLarge());
//    }

//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void noTalkWithSuchId() throws Exception {
//        when(talkService.findTalkById(1L)).thenThrow(new TalkNotFoundException());
//        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1, "comment", IN_PROGRESS))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("error", is(TalkController.TALK_NOT_FOUND)));
//    }

    @Test
    public void handleTalkNotFoundCorrectStatus() throws Exception {
        ResponseEntity<MessageDto> response = talkController.handleTalkNotFound();
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void handleTalkNotFoundCorrectMessage() throws Exception {
        ResponseEntity<MessageDto> response = talkController.handleTalkNotFound();
        assertThat(response.getBody().getError(), is(TalkController.TALK_NOT_FOUND));
    }

    @Test
    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
    public void correctChangeTalkBySpeaker() throws Exception {
        when(talkService.findTalkById(anyLong())).thenReturn(createTalk(createUser()));
        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1, correctTalkDto))
                .andExpect(status().isOk());
    }

//    @Test
//    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
//    public void tryChangeRejectedTalkBySpeaker() throws Exception {
//        Talk talk = createTalk(createUser());
//        talk.setStatus(TalkStatus.REJECTED);
//        when(talkService.findTalkById(anyLong())).thenReturn(talk);
//        TalkDto rejectedTalk = setupCorrectTalkDto();
//        rejectedTalk.setStatusName(REJECTED);
//        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1, rejectedTalk))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockUser(username = SPEAKER_EMAIL, roles = SPEAKER_ROLE)
//    public void tryChangeApprovedTalkBySpeaker() throws Exception {
//        Talk talk = createTalk(createUser());
//        talk.setStatus(TalkStatus.APPROVED);
//        when(talkService.findTalkById(2L)).thenReturn(talk);
//        TalkDto approvedTalk = setupCorrectTalkDto();
//        approvedTalk.setStatusName(APPROVED);
//        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 2, approvedTalk))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void tryNullStatusActionOnTalk() throws Exception {
//        Talk talk = createTalk(createUser());
//        when(talkService.findTalkById(anyLong())).thenReturn(talk);
//        TalkDto rejectedTalk = setupCorrectTalkDto();
//        rejectedTalk.setStatusName(null);
//        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 1, rejectedTalk))
//                .andExpect(status().isBadRequest());
//    }

//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void tryWrongStatus() throws Exception {
//        Talk talk = createTalk(createUser());
//        when(talkService.findTalkById(anyLong())).thenReturn(talk);
//        TalkDto newTalk = setupCorrectTalkDto();
//        newTalk.setStatusName(TalkStatus.NEW.getName());
//        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 3, newTalk))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockUser(username = ORGANISER_EMAIL, roles = ORGANISER_ROLE)
//    public void tryChangeIntoStatusThatNotAllow() throws Exception {
//        Talk talk = createTalk(createUser());
//        talk.setStatus(TalkStatus.APPROVED);
//        when(talkService.findTalkById(anyLong())).thenReturn(talk);
//        TalkDto newTalk = setupCorrectTalkDto();
//        newTalk.setStatusName(TalkStatus.IN_PROGRESS.getName());
//        mockMvc.perform(preparePatchRequest(API_TALK + "/" + 4, newTalk))
//                .andExpect(status().isConflict());
//    }

    private MockHttpServletRequestBuilder prepareGetRequest(String uri) throws Exception {
        return MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8);
    }

    private MockHttpServletRequestBuilder preparePostRequest(String uri) throws JsonProcessingException {
        return MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(correctTalkDto));
    }

    private MockHttpServletRequestBuilder preparePatchRequest(String uri, String comment, String status) throws JsonProcessingException {
        return MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(setupCorrectDtoForOrganiserChangeStatus(comment, status)));
    }

    private MockHttpServletRequestBuilder preparePatchRequest(String uri, TalkDto dto) throws JsonProcessingException {
        return MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(dto));
    }

    private void expectUnauthorized(ResultActions ra) throws Exception {
        ra.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("error", is(ApplicationControllerAdvice.UNAUTHORIZED_MSG)));
    }

    private Talk createTalk(User user) {
        Topic topic = new Topic(1L, "Topic");
        Type type = new Type(1L, "Type");
        Language language = new Language(1L, "Language");
        Level level = new Level(1L, "Level");
        Talk talk = new Talk();
        talk.setId(1L);
        talk.setUser(user);
        talk.setStatus(TalkStatus.NEW);
        talk.setTopic(topic);
        talk.setType(type);
        talk.setLanguage(language);
        talk.setLevel(level);
        talk.setTime(LocalDateTime.now());
        talk.setTitle("Title");
        talk.setDescription("Descr");
        talk.setAdditionalInfo("Add Info");
        return talk;
//        return new Talk(1L, user, TalkStatus.NEW, topic, type, language, level, LocalDateTime.now(), "Title", "Descr", "Add Info", null, null, null);
    }

    private Talk createTalk(User speaker, User organiser) {
        Talk talk = createTalk(speaker);
        talk.setOrganiser(organiser);
        return talk;
    }

    private TalkDto setupCorrectTalkDto() {
        TalkDto correctTalkDto = new TalkDto();
        correctTalkDto.setId(1L);
        correctTalkDto.setDescription("Description");
        correctTalkDto.setTitle("Title");
        correctTalkDto.setLanguageName("English");
        correctTalkDto.setLevelName("Beginner");
        correctTalkDto.setStatusName(TalkController.DEFAULT_TALK_STATUS);
        correctTalkDto.setTypeName("Regular Talk");
        correctTalkDto.setTopicName("JVM Languages and new programming paradigms");
        correctTalkDto.setDate(LocalDateTime.now().toString());
        correctTalkDto.setAdditionalInfo("Info");
        correctTalkDto.setOrganiserComment("Org comment");
        correctTalkDto.setUserId(1L);
        return correctTalkDto;
    }

    private TalkDto setupCorrectDtoForOrganiserChangeStatus(String comment, String status) {
        TalkDto talkDto = new TalkDto();
        talkDto.setOrganiserComment(comment);
        talkDto.setStatusName(status);
        return talkDto;
    }

    private byte[] convertObjectToJsonBytes(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    private String createStringWithLength(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append('x');
        }
        return stringBuilder.toString();
    }
}


