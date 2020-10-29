package com.myprojects.bety2.classes;

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
public class Home {

    @Setter @Getter
    private String name, _id;
    @Setter @Getter
    private List<String> admins; // List of IDs
    @Setter @Getter
    private int stuffs; // List of IDs
    @Setter @Getter
    private int members; // List of IDs
    @Setter @Getter
    private Date created;

}
