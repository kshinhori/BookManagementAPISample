# Book Management API

## 概要
Spring BootとjOOQを使用した書籍管理APIのサンプルです。
タイトルや著者名に基づいて書籍情報を検索・取得する機能を提供します。

## 技術スタック
- Spring Boot
- jOOQ
- Kotlin
- H2 Database

# 使い方
以下のエンドポイントで書籍情報の検索が可能です。

GET /bookmanagement/search/books: タイトルまたは著者名による書籍の検索

詳細: docs/api-documentation.md