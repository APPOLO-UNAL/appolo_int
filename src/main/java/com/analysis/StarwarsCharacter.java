package com.analysis;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StarwarsCharacter {
    private int Id;
    private String Name;
    private Date joinDate;
}