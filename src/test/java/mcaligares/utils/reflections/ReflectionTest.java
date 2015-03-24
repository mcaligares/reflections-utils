/*
 * Copyright 2015 Miguel Augusto Caligares <mcaligares@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mcaligares.utils.reflections;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;

import mcaligares.utils.reflections.annotations.AnnotationInClass;
import mcaligares.utils.reflections.annotations.AnnotationInField;
import mcaligares.utils.reflections.annotations.OtherAnnotationInField;
import mcaligares.utils.reflections.beans.BeanWithAnnotations;
import mcaligares.utils.reflections.beans.BeanWithoutAnnotations;

/**
 * 
 * @author miguel
 *
 */
public class ReflectionTest {

    @Test
    public void testAnnotationClass() {
        Class<BeanWithAnnotations> classWithAnnotation = BeanWithAnnotations.class;
        Class<BeanWithoutAnnotations> classWithoutAnnotation = BeanWithoutAnnotations.class;

        Object annotationClass;
        annotationClass = ReflectionUtils.getAnnotationClass(classWithAnnotation, AnnotationInClass.class);
        assertThat(annotationClass, allOf(notNullValue(), is(AnnotationInClass.class)));

        annotationClass = ReflectionUtils.getAnnotationClass(classWithoutAnnotation, AnnotationInClass.class);
        assertThat(annotationClass, allOf(nullValue()));

        Boolean hasAnnotationInClass;
        hasAnnotationInClass = ReflectionUtils.hasAnnotationClass(classWithAnnotation, AnnotationInClass.class);
        assertThat(hasAnnotationInClass, is(true));

        hasAnnotationInClass = ReflectionUtils.hasAnnotationClass(classWithoutAnnotation, AnnotationInClass.class);
        assertThat(hasAnnotationInClass, is(false));
    }

    @Test
    public void testAnnotationField() {
        BeanWithAnnotations beanWithAnnotations = new BeanWithAnnotations();

        assertThat(ReflectionUtils.getFields(beanWithAnnotations.getClass()), notNullValue());
        assertThat(ReflectionUtils.getFields(beanWithAnnotations.getClass()).size(), is(5));

        assertThat(ReflectionUtils.getFieldsInClass(beanWithAnnotations.getClass()), notNullValue());
        assertThat(ReflectionUtils.getFieldsInClass(beanWithAnnotations.getClass()).size(), is(4));

        assertThat(ReflectionUtils.getFieldsInSuperclass(beanWithAnnotations.getClass()), notNullValue());
        assertThat(ReflectionUtils.getFieldsInSuperclass(beanWithAnnotations.getClass()).size(), is(1));

        BeanWithoutAnnotations beanWithoutAnnotations = new BeanWithoutAnnotations();

        assertThat(ReflectionUtils.getFields(beanWithoutAnnotations.getClass()), notNullValue());
        assertThat(ReflectionUtils.getFields(beanWithoutAnnotations.getClass()).size(), is(4));

        assertThat(ReflectionUtils.getFieldsInClass(beanWithoutAnnotations.getClass()), notNullValue());
        assertThat(ReflectionUtils.getFieldsInClass(beanWithoutAnnotations.getClass()).size(), is(3));

        assertThat(ReflectionUtils.getFieldsInSuperclass(beanWithoutAnnotations.getClass()), notNullValue());
        assertThat(ReflectionUtils.getFieldsInSuperclass(beanWithoutAnnotations.getClass()).size(), is(1));
    }

    @Test
    public void testFindAnnotationField() {
        List<Field> fields;
        BeanWithAnnotations beanWithAnnotations = new BeanWithAnnotations();

        assertThat(ReflectionUtils.getFieldByName(beanWithAnnotations, "id"), notNullValue());
        assertThat(ReflectionUtils.getFieldByName(beanWithAnnotations, "number"), notNullValue());
        assertThat(ReflectionUtils.getFieldByName(beanWithAnnotations, "name"), notNullValue());
        assertThat(ReflectionUtils.getFieldByName(beanWithAnnotations, "fieldWithoutAnnotation"), notNullValue());
        assertThat(ReflectionUtils.getFieldByName(beanWithAnnotations, "fieldWithBothAnnotations"), notNullValue());

        assertThat(ReflectionUtils.getFieldByName(beanWithAnnotations, "ids"), nullValue());
        assertThat(ReflectionUtils.getFieldByName(beanWithAnnotations, "field"), nullValue());

        fields = ReflectionUtils.getFieldsWithAnnotations(beanWithAnnotations.getClass(), AnnotationInField.class);
        assertThat(fields, notNullValue());
        assertThat(fields.size(), is(2));

        fields = ReflectionUtils.getFieldsWithAllAnnotations(beanWithAnnotations.getClass(), AnnotationInField.class,
                OtherAnnotationInField.class);
        assertThat(fields, notNullValue());
        assertThat(fields.size(), is(1));

        fields = ReflectionUtils.getFieldsWithAnyAnnotations(beanWithAnnotations.getClass(), AnnotationInField.class,
                OtherAnnotationInField.class);
        assertThat(fields, notNullValue());
        assertThat(fields.size(), is(3));

        BeanWithoutAnnotations beanWithoutAnnotations = new BeanWithoutAnnotations();

        assertThat(ReflectionUtils.getFieldByName(beanWithoutAnnotations, "id"), notNullValue());
        assertThat(ReflectionUtils.getFieldByName(beanWithoutAnnotations, "name"), notNullValue());
        assertThat(ReflectionUtils.getFieldByName(beanWithoutAnnotations, "years"), notNullValue());
        assertThat(ReflectionUtils.getFieldByName(beanWithoutAnnotations, "things"), notNullValue());

        assertThat(ReflectionUtils.getFieldByName(beanWithoutAnnotations, "ids"), nullValue());
        assertThat(ReflectionUtils.getFieldByName(beanWithoutAnnotations, "field"), nullValue());

        fields = ReflectionUtils.getFieldsWithAnnotations(beanWithoutAnnotations.getClass(), AnnotationInClass.class);
        assertThat(fields, nullValue());
    }

    @Test
    public void testField() {
        Field field;
        BeanWithAnnotations beanWithAnnotations = new BeanWithAnnotations();

        field = ReflectionUtils.getFieldByName(beanWithAnnotations, "name");
        assertThat(ReflectionUtils.getValue(beanWithAnnotations, field), nullValue());

        ReflectionUtils.setValue(beanWithAnnotations, "miguel", field);
        assertThat(beanWithAnnotations.getName(), allOf(notNullValue(), is(String.class), is("miguel")));

        BeanWithoutAnnotations beanWithoutAnnotations = new BeanWithoutAnnotations();

        field = ReflectionUtils.getFieldByName(beanWithoutAnnotations, "name");
        assertThat(ReflectionUtils.getValue(beanWithoutAnnotations, field), nullValue());

        ReflectionUtils.setValue(beanWithoutAnnotations, "miguel", field);
        assertThat(beanWithoutAnnotations.getName(), allOf(notNullValue(), is(String.class), is("miguel")));
    }

    @Test
    public void testHasAnnotation() {
        Field field;
        BeanWithAnnotations beanWithAnnotations = new BeanWithAnnotations();
        field = ReflectionUtils.getFieldByName(beanWithAnnotations, "number");
        assertThat(ReflectionUtils.hasAnnotations(field, AnnotationInField.class), is(true));
        assertThat(ReflectionUtils.hasAnyAnnotations(field, AnnotationInField.class, OtherAnnotationInField.class),
                is(true));
        assertThat(ReflectionUtils.hasAllAnnotations(field, AnnotationInField.class, OtherAnnotationInField.class),
                is(false));

        BeanWithoutAnnotations beanWithoutAnnotations = new BeanWithoutAnnotations();
        field = ReflectionUtils.getFieldByName(beanWithoutAnnotations, "name");
        assertThat(ReflectionUtils.hasAnnotations(field, AnnotationInField.class), is(false));
    }

}
