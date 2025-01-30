import js from '@eslint/js'
import globals from 'globals'
import tseslint from 'typescript-eslint'
import prettierPlugin from 'eslint-plugin-prettier'
import onlyWarn from 'eslint-plugin-only-warn'
import reactHooks from 'eslint-plugin-react-hooks'
import simpleImportSort from 'eslint-plugin-simple-import-sort'
import unusedImports from 'eslint-plugin-unused-imports'

export default tseslint.config(
  { ignores: ['dist', 'node_modules', '**/*.config.js', '!**/eslint.config.js'] },
  {
    extends: [
      js.configs.recommended,
      ...tseslint.configs.recommended,
      ...tseslint.configs.stylistic,
      ...tseslint.configs.strict
    ],
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
      parser: tseslint.parser,
      parserOptions: {
        ecmaVersion: 2020,
        sourceType: 'module',
        ecmaFeatures: {
          jsx: true
        }
      }
    },
    plugins: {
      'react-hooks': reactHooks,
      'unused-imports': unusedImports,
      prettier: prettierPlugin,
      'simple-import-sort': simpleImportSort,
      '@typescript-eslint': tseslint.plugin,
      onlyWarn
    },
    rules: {
      ...reactHooks.configs.recommended.rules,
      'import/no-anonymous-default-export': 'off',
      'react-refresh/only-export-components': ['warn', { allowConstantExport: true }],
      'prettier/prettier': 'error',
      'unused-imports/no-unused-imports': 'error',
      'unused-imports/no-unused-vars': 'error',
      'simple-import-sort/imports': 'error',
      'simple-import-sort/exports': 'error',
      'no-unused-vars': 'off',
      'prefer-const': 'error',
      '@typescript-eslint/no-explicit-any': 'error',
      '@typescript-eslint/no-unused-vars': 'error',
      '@typescript-eslint/no-empty-function': 'error',
      '@typescript-eslint/no-empty-object-type': 'error',
      '@typescript-eslint/no-empty-interface': 'error',
      '@typescript-eslint/no-unused-vars': 'error'
    }
  }
)
