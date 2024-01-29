package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class JdbcUserDaoTests extends BaseDaoTests{

    private static final User USER_1 = new User(1, "TEST_USER_1", "password1", "o" );
    private static final User USER_2 = new User(2, "TEST_USER_2", "password2", "o" );
    private static final User USER_3 = new User(3, "TEST_USER_3", "password3", "o" );

    private User testUser;

    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
        testUser = new User(0, "TEST_USER_0","password0", "o" );
    }

//    @Test
//    public void findIdByUsername_returns_correct_user() {
//        int userId1 = sut.findIdByUsername("TEST_USER_1");
//        int userId2 = sut.findIdByUsername("TEST_USER_2");
//
//        // Assert
//        assertEquals(USER_1.getId(), userId1);
//        assertEquals(USER_2.getId(), userId2);
////        User user = sut.findIdByUsername(USER_1);
////        assertUsersMatch(USER_1, user);
////
////        user = sut.findIdByUsername("TEST_USER_2");
////        assertUsersMatch(USER_2, user);

//    }

//    @Test
//    public void findByUsername_returns_correct_user() {
//        List<User> users = sut.findAll();
//
//        Assert.assertEquals(2, users.size());
//
//        assertUsersMatch(USER_1, users.get(0));
//        assertUsersMatch(USER_2, users.get(1));
//        assertUsersMatch(USER_3, users.get(2));
//    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }
//    private void assertUsersMatch(User expected, User actual) {
//        Assert.assertEquals(expected.getId(), actual.getId());
//        Assert.assertEquals(expected.getUsername(), actual.getUsername());
//        Assert.assertEquals(expected.getPassword(), actual.getPassword());
//        Assert.assertEquals(expected.getAuthorities(), actual.getAuthorities());
//
//    }

}
