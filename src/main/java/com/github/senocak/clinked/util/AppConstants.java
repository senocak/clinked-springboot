package com.github.senocak.clinked.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public interface AppConstants {
    String DEFAULT_PAGE_NUMBER = "0";
    String DEFAULT_PAGE_SIZE = "10";
    String TOKEN_HEADER_NAME = "Authorization";
    String TOKEN_PREFIX = "Bearer ";
    String ADMIN = "ADMIN";
    String USER = "USER";

    @Getter
    @AllArgsConstructor
    enum RoleName {
        ROLE_USER(USER),
        ROLE_ADMIN(ADMIN);
        private final String role;
    }

    /**
     * @param input -- string variable to make it sluggable
     * @return -- sluggable string variable
     */
    static String toSlug(String input) {
        Pattern non_latin = Pattern.compile("[^\\w-]");
        Pattern white_space = Pattern.compile("[\\s]");
        String no_white_space = white_space.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(no_white_space, Normalizer.Form.NFD);
        String slug = non_latin.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
