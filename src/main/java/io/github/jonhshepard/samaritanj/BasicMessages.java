package io.github.jonhshepard.samaritanj;

import java.util.Arrays;
import java.util.List;

/**
 * @author JonhSHEPARD
 */
enum BasicMessages {

    PRESENTATION(Arrays.asList("I'm", "Samaritan,", "An", "Micro-Artificial", "Intelligence", "Made", "In", "Java", "And", "JavaFX", "For", "UI")),
    AUTHOR(Arrays.asList("My", "Creator", "Is", "Bussignies", "Jean-Baptiste", "Also", "Known", "By", "JonhSHEPARD", "Or", "ADMIN", "For", "Me")),
    END(Arrays.asList("I'm", "Going", "To", "Disappear"));

    private List<String> messages;

    BasicMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessage() {
        return this.messages;
    }

}
