package org.swmaestro.mohaeng.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("모든 정보를 올바른 형식으로 입력하면 회원이 생성된다")
    void createUser() {
        assertDoesNotThrow(
                () -> User.createUser("test@mohaeng.org", "test", "test", "test", "test")
        );
    }

    @Test
    @DisplayName("회원의 프로필 이미지가 존재하면 해당 이미지 url을 올바르게 반환한다")
    void getImgUrlWhenHasImage() {
        String expected = "testImage.jpg";
        User user = User.createUser("test@mohaeng.org", "test", "test", "test", expected);

        String actual = user.getImageUrl();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("회원 프로필 이미지를 변경하면 변경 전 이미지는 삭제하고 프로필 이미지는 올바르게 변경된다.")
    void updateProfileImage() {
        String expected = "testImage.jpg";
        User user = User.createUser("test@mohaeng.org", "test", "test", "test", "beforeImage.jpg");

        user.updateProfileImage(expected);

        assertThat(user.getImageUrl()).isEqualTo(expected);
    }
}