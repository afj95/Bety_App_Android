package com.myprojects.bety2.classes;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stuff implements Serializable { //, Comparable {

    @Getter
    private String Id, stuff, quantity;
    @Setter @Getter
    private String homeId;
    @Setter @Getter
    private boolean isChecked, isDeleted;

//    @Override
//    public int compareTo(Object o) {
//        int compareId = Integer.parseInt(((Stuff) o).getStuff());
//        return ( Integer.parseInt(this.stuff) - compareId);
//    }
}