package ua.rd.cm.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.rd.cm.domain.UserInfo;
import ua.rd.cm.repository.UserInfoRepository;
import ua.rd.cm.repository.specification.userinfo.UserInfoById;
import ua.rd.cm.services.impl.UserInfoServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleUserInfoServiceTest {
    @Mock
    private UserInfoRepository userInfoRepository;

    private UserInfoService userInfoService;
    private UserInfo userInfo;

    @Before
    public void setUp() {
        userInfoService = new UserInfoServiceImpl(userInfoRepository);
        userInfo = mock(UserInfo.class);
    }

    @Test
    public void testSaveUserInfo() {
        userInfoService.save(userInfo);
        verify(userInfoRepository).saveUserInfo(userInfo);
    }

    @Test
    public void testUpdateUserInfo() {
        userInfoService.update(userInfo);
        verify(userInfoRepository).updateUserInfo(userInfo);
    }

    @Test
    public void testFindByIdUserInfo() {
        List<UserInfo> list = new ArrayList<UserInfo>(){{
            UserInfo userInfo = new UserInfo();
            userInfo.setId(1L);
            userInfo.setShortBio("bio");
            userInfo.setJobTitle("job");
            userInfo.setPastConference("pastConf");
            userInfo.setCompany("company");
            userInfo.setAdditionalInfo("info");
            add(userInfo);
        }};
        when(userInfoRepository.findBySpecification(new UserInfoById(anyLong()))).thenReturn(list);
        UserInfo userInfo = userInfoService.find(1L);
        assertEquals(userInfo.getId(), list.get(0).getId());
        assertEquals(userInfo, list.get(0));
    }
}
