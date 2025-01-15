module.exports = {
    root: true,

    // `extends`는 기본 규칙을 확장하는 데 사용됩니다. 이 설정은 여러 기본 설정을 가져옵니다.
    extends: [
        'expo', // Expo 프로젝트에 맞춘 기본 ESLint 설정
        'prettier', // Prettier와 충돌하지 않도록 기본 ESLint 규칙 비활성화
        'eslint:recommended', // ESLint의 권장 설정
        'plugin:react/recommended', // React 프로젝트를 위한 권장 설정
        'plugin:@typescript-eslint/recommended', // TypeScript 프로젝트를 위한 권장 설정
        'plugin:prettier/recommended' // Prettier와 함께 사용하기 위한 설정
    ],
    // `plugins`는 추가적인 ESLint 플러그인을 정의합니다.
    plugins: [
        'prettier', // Prettier 통합을 위한 플러그인
        '@typescript-eslint', // TypeScript 지원을 위한 플러그인
        'unused-imports', // 사용되지 않는 import를 감지하고 제거하기 위한 플러그인
        'simple-import-sort' // import 구문을 정렬하기 위한 플러그인
    ],
    // `rules`는 특정 규칙을 설정합니다.
    rules: {
        // 일관된 타입 임포트를 강제합니다.
        '@typescript-eslint/consistent-type-imports': 'error',
        // Prettier 규칙을 강제로 적용합니다.
        'prettier/prettier': 'error',
        // JSX에서 중괄호를 사용하지 않도록 강제합니다.
        'react/jsx-curly-brace-presence': ['error', { props: 'never', children: 'never' }],
        // import 구문을 정렬합니다.
        'simple-import-sort/imports': 'error',
        // export 구문을 정렬합니다.
        'simple-import-sort/exports': 'error',
        // any 타입 사용을 금지합니다.
        '@typescript-eslint/no-explicit-any': 'error',
        // const를 선호합니다.
        'prefer-const': 'error',
        // 일관된 네이밍 규칙을 강제합니다.
        '@typescript-eslint/naming-convention': [
            'error',
            {
                selector: 'variable', // 변수는 camelCase 또는 UPPER_CASE를 사용해야 합니다.
                format: ['camelCase', 'UPPER_CASE']
            },
            {
                selector: 'function', // 함수는 camelCase 또는 PascalCase를 사용해야 합니다.
                format: ['camelCase', 'PascalCase']
            },
            {
                selector: 'typeLike', // 타입과 인터페이스는 PascalCase를 사용해야 합니다.
                format: ['PascalCase']
            }
        ],
        // 사용되지 않는 import를 금지합니다.
        'unused-imports/no-unused-imports': 'error',
        // 사용되지 않는 변수를 금지합니다.
        'unused-imports/no-unused-vars': 'error',
        // React 17 이상에서는 JSX를 사용하기 위해 React를 import할 필요가 없습니다.
        'react/react-in-jsx-scope': 'off'
    }
};
