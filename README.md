# 📞 비슷코드

## 🙌 팀 소개

비비빅팀(BBeBig)은 '크게 성장하는 B팀'을 의미합니다. 의사소통과 피드백에 열정적인 5명의 팀원이 모여있습니다.

|Android|BE|BE|FE|FE|
| :-: | :-: | :-: | :-: | :-: |
| <img src="https://avatars.githubusercontent.com/u/59912150?v=4" width="150"> |<img src="https://avatars.githubusercontent.com/u/51228946?v=4" width="150"> |<img src="https://avatars.githubusercontent.com/u/83596813?v=4" width="150"> |<img src="https://avatars.githubusercontent.com/u/101444425?v=4" width="150">|<img src="https://avatars.githubusercontent.com/u/115636461?v=4" width="150">|
| [서정우](https://github.com/SEO-J17)|[백도현](https://github.com/dh1010a)|[이소은](https://github.com/soeun2537)|[김예지](https://github.com/mnbvcxzyj)| [이지형](https://github.com/Zero-1016)|


## 💬 프로젝트 목적

> 개발자로서 성장하기 위해 한계를 마주하기

새로운 기술과 혼자서는 도전하기 어려운 영역을 한계로 정의했습니다. 팀원들과의 페어 프로그래밍과 세미나를 통해 함께 학습하며 이러한 한계를 극복하고 새로운 가능성을 확장해 나갑니다.

디자인과 기획을 참고하여 개발에만 집중할 수 있도록 클론 프로젝트 방식을 선택했습니다.

## 💁 작업에 참여하는 방법

필요한 기능 및 버그를 미리 작성된 템플릿을 기반으로 이슈를 생성합니다. 작업량에 따라 PR, Commit 컨벤션에 맞게 작성을 진행합니다.

### ✅ PR 컨벤션

```
[${플랫폼}] ${설명} #${이슈 번호}
```
ex: 
  - [BE] 카프카 연동하여 소비/발행 이벤트 및 채팅 구현 #70
  - [Android] 개인 메시지 화면 레이아웃 구현 #73

### ✅ Commit 컨벤션

```
${타입}: ${설명}
```

### ✅ Commit 타입

- `feat`: 새로운 기능이 추가
- `fix`: 버그 수정
- `build`: 빌드 시스템 또는 외부 종속성 관련 변경 (예: Gradle 설정)
- `refactor`: 코드 리팩토링, 유지 보수
- `chore`: 코드 변경이 아닌 설정 변경
- `docs`: 문서 수정 (예: README, API 문서)
- `style`: 코드 스타일 또는 포맷 변경
- `test`: 테스트 코드 추가 또는 수정
- `perf`: 성능 개선 작업
- `revert`: 이전 커밋을 되돌릴 때
- `ci`: CI 설정 파일 및 스크립트 변경
- `cd`: CD 설정 파일 및 스크립트 변경

ex:
  - feat: 알림 이벤트 소비 및 발행 기능 구현
  - fix: consumer 서비스에서 예외처리 반대로 되있는 로직 수정
  - feat: 채널 전환 이벤트 발행 기능 구현
  - feat: 메시지 발행시 snowFlake로 ID 생성하여 카프카 발행 기능 구현
  - chore: 카프카 패키지 구조 분리

### 🎶 Branch 컨벤션

```
${플랫폼}/${타입}/#${이슈 번호}
```

### ✅ Branch 전략
본 프로젝트는 Git Flow 전략을 변형하여 사용합니다.

1. 플랫폼에 맞춰 브랜치를 분리하여 작업합니다.
2. 기능 개발은 `feature` 브랜치에서 진행합니다.
3. 기능 브랜치의 작업 내용은 `main` 브랜치에 직접 병합하지 않고, 먼저 `dev` 브랜치에 병합합니다.
4. 통합 테스트 및 검증이 완료되면 `dev` 브랜치에서 `main` 브랜치로 최종 병합합니다.


<img width="1208" alt="image" src="https://github.com/user-attachments/assets/93b3422e-8b28-49bc-9696-e6dc33ae3485" />
