package com.example.campusfinder.core.configuration;

import com.example.campusfinder.user.dto.request.element.EmailDto;
import com.example.campusfinder.user.dto.request.element.PhoneNumberDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.example.campusfinder.core.configuration
 * fileName       : ModelMapperConfiguration
 * author         : tlswl
 * date           : 2024-09-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-06        tlswl       최초 생성
 */
@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // EmailDto -> String 매핑
        Converter<EmailDto, String> emailConverter = new Converter<EmailDto, String>() {
            @Override
            public String convert(MappingContext<EmailDto, String> context) {
                return context.getSource().email();
            }
        };

        // PhoneNumberDto -> String 매핑
        Converter<PhoneNumberDto, String> phoneConverter = new Converter<PhoneNumberDto, String>() {
            @Override
            public String convert(MappingContext<PhoneNumberDto, String> context) {
                return context.getSource().phoneNumber();
            }
        };

        modelMapper.addConverter(emailConverter);
        modelMapper.addConverter(phoneConverter);

        return modelMapper;
    }
}
