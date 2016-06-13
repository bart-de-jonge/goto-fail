package data;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class UserTest {
    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User("testName", User.Role.CAMERA_OPERATOR, new ArrayList<>());
    }

    @Test
    public void defaultConstructor() {
        User myUser = new User();
        assertEquals("", myUser.getName());
        assertEquals(User.Role.NONE, myUser.getRole());
        assertEquals(-1, myUser.getRoleValue());
        assertEquals(null, myUser.getChosenTimelines());
    }

    @Test
    public void setRole() {
        user.setRole(User.Role.DIRECTOR);
        assertEquals(User.Role.DIRECTOR, user.getRole());
        assertEquals(2, user.getRoleValue());
    }

    @Test
    public void setName() {
        user.setName("testName2");
        assertEquals("testName2", user.getName());
    }

    @Test
    public void setRoleValue0() {
        user.setRoleValue(0);
        assertEquals(0, user.getRoleValue());
        assertEquals(User.Role.CAMERA_OPERATOR, user.getRole());
    }

    @Test
    public void setRoleValue1() {
        user.setRoleValue(1);
        assertEquals(1, user.getRoleValue());
        assertEquals(User.Role.SHOT_CALLER, user.getRole());
    }

    @Test
    public void setRoleValue2() {
        user.setRoleValue(2);
        assertEquals(2, user.getRoleValue());
        assertEquals(User.Role.DIRECTOR, user.getRole());
    }

    @Test
    public void setRoleValueDefault() {
        user.setRoleValue(-1);
        assertEquals(-1, user.getRoleValue());
        assertEquals(User.Role.NONE, user.getRole());
    }

    @Test
    public void setChosenTimelines() {
        ArrayList<Integer> timelines = new ArrayList<>(Arrays.asList(1, 2, 3));
        user.setChosenTimelines(timelines);
        assertEquals(timelines, user.getChosenTimelines());
        assertEquals(3, user.getChosenTimelines().size());
    }

    @Test
    public void getName() {
        assertEquals("testName", user.getName());
    }

    @Test
    public void getRole() {
        assertEquals(User.Role.CAMERA_OPERATOR, user.getRole());
    }

    @Test
    public void getRoleValue() {
        assertEquals(0, user.getRoleValue());
    }

    @Test
    public void getChosenTimelines() {
        assertEquals(0, user.getChosenTimelines().size());
    }
}