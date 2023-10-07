package com.basejava.webapp.model;

public enum ContactType {
    PHONE("Tel.: "),
    SKYPE("Skype: ") {
        @Override
        public String toHtml0(String value) {
            return getContact() + ": " + toLink("skype:" + value, value);
        }
    },
    EMAIL("Email: ") {
        @Override
        public String toHtml0(String value) {
            return getContact() + ": " + toLink("mailto:" + value, value);
        }
    },
    LINKEDIN("Profile LinkedIn: ") {
        @Override
        public String toHtml0(String value) {
            return toLink(value);
        }
    },
    GITHUB("Profile GitHub: ") {
        @Override
        public String toHtml0(String value) {
            return toLink(value);
        }
    },
    STACKOVERFLOW("Profile Stackoverflow: ") {
        @Override
        public String toHtml0(String value) {
            return toLink(value);
        }
    },
    URL_HOMEPAGE("Home page") {
        @Override
        public String toHtml0(String value) {
            return toLink(value);
        }
    };

    private final String contact;

    ContactType(String contact) {
        this.contact = contact;
    }

    public final String getContact() {
        return contact;
    }

    protected String toHtml0(String value) {
        return contact + ": " + value;
    }

    public String toHtml(String value) {
        return (value == null) ? "" : toHtml0(value);
    }

    public String toLink(String href) {
        return toLink(href, contact);
    }

    public static String toLink(String href, String title) {
        return "<a href='" + href + "'>" + title + "</a>";
    }
}