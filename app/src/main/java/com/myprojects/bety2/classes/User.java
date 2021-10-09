package com.myprojects.bety2.classes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Setter @Getter
    private String firstName, lastName, username, email, password;
    @Setter @Getter
    @SerializedName("phoneNumber")
    private long phoneNumber;
    @Setter @Getter
    @SerializedName("homes")
    private List<String> homes = new ArrayList<>();
    @Setter @Getter
    @SerializedName("stuffs")
    private List<Stuff> stuffs = new ArrayList<>();
    @Getter
    private String joined;

}
