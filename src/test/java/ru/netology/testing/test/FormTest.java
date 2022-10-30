package ru.netology.testing.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testing.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testing.data.DataGenerator.Registration.getUser;
import static ru.netology.testing.data.DataGenerator.getRandomLogin;
import static ru.netology.testing.data.DataGenerator.getRandomPassword;

public class FormTest {

    @BeforeEach
    void setup() {
        Selenide.open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login'] .button__content").click();
        $$(".heading").findBy(text("  Личный кабинет")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        Selenide.$("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        Selenide.$("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        Selenide.$("[data-test-id='action-login'] .button__content").click();
        Selenide.$("[data-test-id='error-notification'] .notification__title").shouldBe(Condition.visible, Condition.exactText("Ошибка"));
        Selenide.$("[data-test-id='error-notification'] .notification__content").shouldBe(Condition.visible, Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        Selenide.$("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        Selenide.$("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        Selenide.$("[data-test-id='action-login'] .button__content").click();
        Selenide.$("[data-test-id='error-notification'] .notification__title").shouldBe(Condition.visible, Condition.exactText("Ошибка"));
        Selenide.$("[data-test-id='error-notification'] .notification__content").shouldBe(Condition.visible, Condition.exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        Selenide.$("[data-test-id='login'] input").setValue(wrongLogin);
        Selenide.$("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        Selenide.$("[data-test-id='action-login'] .button__content").click();
        Selenide.$("[data-test-id='error-notification'] .notification__title").shouldBe(Condition.visible, Condition.exactText("Ошибка"));
        Selenide.$("[data-test-id='error-notification'] .notification__content").shouldBe(Condition.visible, Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        Selenide.$("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        Selenide.$("[data-test-id='password'] input").setValue(wrongPassword);
        Selenide.$("[data-test-id='action-login'] .button__content").click();
        Selenide.$("[data-test-id='error-notification'] .notification__title").shouldBe(Condition.visible, Condition.exactText("Ошибка"));
        Selenide.$("[data-test-id='error-notification'] .notification__content").shouldBe(Condition.visible, Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }
}