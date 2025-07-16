package com.example.rural_tutoring.model.VO;

import com.example.rural_tutoring.entity.Lesson;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class LessonVO extends Lesson {
    private List<String> imagesUrl;
}
