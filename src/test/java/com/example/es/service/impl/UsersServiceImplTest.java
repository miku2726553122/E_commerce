package com.example.es.service.impl;

import com.example.es.entity.Users;
import com.example.es.mapper.UsersMapper;
import com.example.es.vo.user.FindUserInfoRspVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceImplTest {
    private UsersMapper usersMapper;
    private UsersServiceImpl service;

    @BeforeEach
    void setUp() {
        usersMapper = mock(UsersMapper.class);
        service = new UsersServiceImpl();
        ReflectionTestUtils.setField(service, "usersMapper", usersMapper);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("demo", null, List.of()));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "https://example.test/avatar.png")
    void currentProfileReturnsTheSavedAvatarIncludingRemoval(String avatar) {
        when(usersMapper.findByUsername("demo")).thenReturn(Users.builder()
                .id(12L)
                .username("demo")
                .nickname("Demo")
                .phone("13800138000")
                .avatar(avatar)
                .build());

        var response = service.FindUserInfo();

        assertTrue(response.isSuccess());
        var profile = assertInstanceOf(FindUserInfoRspVO.class, response.getData());
        assertEquals(12L, profile.getId());
        assertEquals("demo", profile.getUsername());
        assertEquals("Demo", profile.getNickname());
        assertEquals("13800138000", profile.getPhone());
        assertEquals(avatar, profile.getAvatar());
    }
}
