module fi.tuni.softwaredesign.eventapp {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires com.google.gson;
    exports fi.tuni.softwaredesign.eventapp;
    exports fi.tuni.softwaredesign.eventapp.view;
    exports fi.tuni.softwaredesign.eventapp.model;
    exports fi.tuni.softwaredesign.eventapp.controller;
}
