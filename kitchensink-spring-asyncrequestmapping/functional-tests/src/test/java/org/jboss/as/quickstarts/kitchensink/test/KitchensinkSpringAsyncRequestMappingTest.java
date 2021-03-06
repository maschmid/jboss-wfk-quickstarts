/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.quickstarts.kitchensink.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.URL;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Kitchensink Spring AsyncRequestMapping quickstart functional test
 *
 * @author Oliver Kiss
 */
@RunAsClient
@RunWith(Arquillian.class)
public class KitchensinkSpringAsyncRequestMappingTest {

    /**
     * Injects browser to our test.
     */
    @Drone
    WebDriver browser;

    /**
     * Injects URL on which application is running.
     */
    @ArquillianResource
    URL contextPath;

    /**
     * Creates deployment which is sent to the container upon test's start.
     *
     * @return war file which is deployed while testing, the whole application in our case
     */
    @Deployment(testable = false)
    public static WebArchive deployment() {
        return Deployments.kitchensink();
    }

    /**
     * Locator for name field
     */
    @FindBy(id = "name")
    WebElement nameField;

    /**
     * Locator for email field
     */
    @FindBy(id = "email")
    WebElement emailField;

    /**
     * Locator for phone number field
     */
    @FindBy(id = "phoneNumber")
    WebElement phoneField;

    /**
     * Locator for registration button
     */
    @FindByJQuery("input.register")
    WebElement registerButton;

    /**
     * Locator for rows of the members table
     */
    @FindByJQuery("table.simpletablestyle:first tbody tr")
    List<WebElement> tableMembersRows;

    /**
     * Locator for columns of the first row of the members table
     */
    @FindByJQuery("table.simpletablestyle:first tbody tr:first td")
    List<WebElement> tableMembersRowColumns;

    /**
     * Locator for name field validation message
     */
    @FindBy(id = "name.errors")
    WebElement nameErrorMessage;

    /**
     * Locator for email field validation message
     */
    @FindBy(id = "email.errors")
    WebElement emailErrorMessage;

    /**
     * Locator for phone number field validation message
     */
    @FindBy(id = "phoneNumber.errors")
    WebElement phoneErrorMessage;

    /**
     * Name of the member to register in the right format.
     */
    private static final String NAME_FORMAT_OK = "John Doe";

    /**
     * Name of the member to register in the bad format.
     */
    private static final String NAME_FORMAT_BAD = "John1";

    /**
     * Name of the member to register which is too long (1-25)
     */
    private static final String NAME_FORMAT_TOO_LONG = "John Doe John Doe John Doe";

    /**
     * E-mail of the member to register in the right format.
     */
    private static final String EMAIL_FORMAT_OK = "john@doe.com";

    /**
     * E-mail of the member to register in the bad format - #1.
     */
    private static final String EMAIL_FORMAT_BAD_1 = "joe";

    /**
     * E-mail of the member to register in the bad format - #2.
     */
    private static final String EMAIL_FORMAT_BAD_2 = "john@doe.com ";

    /**
     * Phone number of the member to register in the right format.
     */
    private static final String PHONE_FORMAT_OK = "0123456789";

    /**
     * Phone number of the member to register in the bad format - illegal
     * characters.
     */
    private static final String PHONE_FORMAT_BAD_ILLEGAL_CHARS = "as/df.123@";

    /**
     * Phone number of the member to register in the bad format - too long.
     */
    private static final String PHONE_FORMAT_BAD_TOO_LONG = "12345678901234567890";

    /**
     * Phone nuber of the member to register in the bad format - too short
     */
    private static final String PHONE_FORMAT_BAD_TOO_SHORT = "123456789";


    @Test
    @InSequence(1)
    public void testEmptyRegistration() {
        browser.get(contextPath.toString());
        guardHttp(registerButton).click();
        assertTrue("Name validation message should be present", nameErrorMessage.isDisplayed());
        assertTrue("Email validation message should be present", emailErrorMessage.isDisplayed());
        assertTrue("PhoneNumber validation message should be present", phoneErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }

    @Test
    @InSequence(2)
    public void testRegistrationWithBadNameFormat() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_BAD, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);
        guardHttp(registerButton).click();
        assertTrue("Name validation message should be present", nameErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_TOO_LONG, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);
        guardHttp(registerButton).click();
        assertTrue("Name validation message should be present", nameErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }

    @Test
    @InSequence(3)
    public void testRegistrationWithBadEmailFormat() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_BAD_1, PHONE_FORMAT_OK);
        guardHttp(registerButton).click();
        assertTrue(emailErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_BAD_2, PHONE_FORMAT_OK);
        guardHttp(registerButton).click();
        assertTrue(emailErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }

    @Test
    @InSequence(4)
    public void testRegistrationWithBadPhoneFormat() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_ILLEGAL_CHARS);
        guardHttp(registerButton).click();
        assertTrue(phoneErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_TOO_SHORT);
        guardHttp(registerButton).click();
        assertTrue(phoneErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_TOO_LONG);
        guardHttp(registerButton).click();
        assertTrue(phoneErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }

    @Test
    @InSequence(5)
    public void testRegularRegistration() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);
        guardHttp(registerButton).click();

        assertEquals(2, tableMembersRows.size());
        assertEquals(5, tableMembersRowColumns.size());

        assertTrue((tableMembersRowColumns.get(1)).getText().equals(NAME_FORMAT_OK));
        assertTrue((tableMembersRowColumns.get(2)).getText().equals(EMAIL_FORMAT_OK));
        assertTrue((tableMembersRowColumns.get(3)).getText().equals(PHONE_FORMAT_OK));
    }

    /**
     * This helper method sets values into the according input fields.
     *
     * @param name  name to set into the name input field
     * @param email email to set into the email input field
     * @param phone phone to set into the phone input field
     */
    private void setInputFields(String name, String email, String phone) {
        nameField.clear();
        nameField.sendKeys(name);
        emailField.clear();
        emailField.sendKeys(email);
        phoneField.clear();
        phoneField.sendKeys(phone);
    }

}
