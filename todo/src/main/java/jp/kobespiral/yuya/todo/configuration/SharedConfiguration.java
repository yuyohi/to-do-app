package jp.kobespiral.yuya.todo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SharedConfiguration {
    // フォームの値と比較するDBから取得したパスワードは暗号化されているのでフォームの値も暗号化するために利用
    /**
     * アプリで共通のパスワード生成器を作る．
     * 
     * @Beanをつけているので任意の箇所でAutowired可能になる
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

}
