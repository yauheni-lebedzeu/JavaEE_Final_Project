package com.gmail.yauheniylebedzeu.service.generator.impl;

import com.gmail.yauheniylebedzeu.service.generator.RandomPasswordGenerator;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Component;

import static org.passay.CharacterCharacteristicsRule.ERROR_CODE;

@Component
public class RandomPasswordGeneratorImpl implements RandomPasswordGenerator {

    private static final int LENGTH_OF_RANDOM_PASSWORD = 10;
    private static final String SPECIAL_CHARS_OF_PASSWORD = "!@#&()";

    @Override
    public String getRandomPassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        CharacterData lowerCaseCharacters = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseCharacters);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseCharacters = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseCharacters);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitCharacters = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitCharacters);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialCharacters = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return SPECIAL_CHARS_OF_PASSWORD;
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialCharacters);
        splCharRule.setNumberOfCharacters(2);

        return passwordGenerator.generatePassword(LENGTH_OF_RANDOM_PASSWORD, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
    }
}