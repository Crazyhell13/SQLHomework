package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.data.SQLSetter;
import ru.netology.web.page.LoginPage;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.web.data.DataHelper.*;

public class AuthTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }
    @AfterAll
    static void clean() {
        SQLSetter.dropDataBase();
    }
    @Test
    @DisplayName("Should login successfully with provided data")
    void shouldEnterWhenValidData() {
        val loginPage = new LoginPage();
        val authInfo = getAuthInfo();
        val verificationPage = loginPage.validAuth(authInfo);
        val verificationCode = SQLSetter.getVerificationCode(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.dashboardPageIsVisible();
    }

    @Test
    @DisplayName("Should not login if username is invalid")
    void shouldNotEnterWhenInvalidLogin() {
        val loginPage = new LoginPage();
        val authInfo = getInvalidLogin();
        loginPage.stepsForAuth(authInfo);
        loginPage.invalidAuth();
    }
    @Test
    @DisplayName("Should not login if password is invalid")
    void shouldNotEnterWhenInvalidPassword() {
        val loginPage = new LoginPage();
        val authInfo = getInvalidPassword();
        loginPage.stepsForAuth(authInfo);
        loginPage.invalidAuth();
    }
    @Test
    @DisplayName("Should not login if verification code is invalid")
    void shouldNotEnterWhenInvalidCode() {
        val loginPage = new LoginPage();
        val authInfo = getAuthInfo();
        val verificationPage = loginPage.validAuth(authInfo);
        val verificationCode = getInvalidVerificationCode();
        verificationPage.stepsForVerify(verificationCode);
        verificationPage.invalidVerify();
    }
    @Test
    @DisplayName("Should not login if password incorrect 3 times in a row")
    void shouldNotEnterWhenInvalidPasswordThreeTimes() {
        val loginPage = new LoginPage();
        val authInfo = getInvalidPassword();
        loginPage.stepsForAuth(authInfo);
        loginPage.invalidAuth();
        loginPage.clearPasswordField();
        loginPage.sendInvalidPassword(authInfo.getPassword());
        loginPage.clearPasswordField();
        loginPage.sendInvalidPassword(authInfo.getPassword());
        loginPage.stepsForAuth(authInfo);
        loginPage.invalidAuth();
    }
}
