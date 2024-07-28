# Hybrid-Vector-Database

## Overview

This project provides a PostgreSQL extension for vector similarity search, vector data storage, and vector dot products. Leveraging Apache Calcite, this extension allows developers to use vector functions seamlessly as if they were normal SQL functions. The project is implemented in Java and integrates directly with PostgreSQL to enhance its capabilities for handling vector data.

## Features

- **Vector Similarity Search**: Efficiently search for similar vectors based on distance metrics.
- **Vector Data Storage**: Store and manage vector data within PostgreSQL.
- **Vector Dot Products**: Compute dot products of vectors directly in SQL queries.
- **Seamless Integration**: Use vector operations as regular SQL functions through PostgreSQL.
- **Apache Calcite Integration**: Leverage Calcite for advanced query processing and optimization.

## Getting Started

### Prerequisites

- Java 8 or higher
- Apache Calcite
- Gradle
- PostgreSQL

### Installation

1. **Clone the Repository:**

    ```bash
    git clone [https://github.com/sanjeeb-sang/Hybrid-Vector-Database](https://github.com/sanjeeb-sang/Hybrid-Vector-Database)
    cd Hybrid-Vector-Database
    ```

2. **Build the Project:**

    Ensure you have Gradle installed. Then build the project using:

    ```bash
    ./gradlew build
    ```
    Or built Project using IntelliJ IDEA.

3. **Run the Application:**

    To Run the Console Application:
    - Execute the main method in Main.java located in src/main/java/bds/Main.java.
  
4. **Queries to Run:**

    Sample Queries to run are in the Queries.txt file located in HybridDatabase1/Queries.txt or <Project Root>/Queries.txt.
    These Queries are also in the Report, so you can get them from there.

### Usage

Once the extension is installed, you can start using the vector functions in your SQL queries. Here are some example usages:

- **Use Vector Functions and Similarity Search in SQL:**

    ```sql
    SELECT id, firstname, lastname, email, hr.SIMILARITY(vec, ARRAY[1.1, 2.2]), hr.SUM_ELEMENTS(vec) as SumElements from hr.employees where age > 30 and hr.SIMILARITY(vec, ARRAY[1.1, 2.2]) > 10 
    ```

    ```sql
    SELECT id, firstname, lastname, email, hr.COSINE_SIMILARITY(vec, ARRAY[1.1, 2.2]) from hr.employees where age > 30 and hr.SIMILARITY(vec, ARRAY[1.1, 2.2]) > 10
    ```

    ```sql
    SELECT id, firstname, lastname, email, hr.COSINE_SIMILARITY(vec, ARRAY[1.1, 2.2]) as CosineSimilarity, hr.SIMILARITY(vec, ARRAY[1.1, 2.2]) as Similarity from hr.employees where age > 30 and hr.SIMILARITY(vec, ARRAY[1.1, 2.2]) > 50 and hr.COSINE_SIMILARITY(vec, ARRAY[1.1, 2.2]) > 0.555 ORDER BY CosineSimilarity DESC

    ```

### Contributing
MIT
Contributions are welcome! Please open an issue or submit a pull request on GitHub.

1. **Fork the Repository**
2. **Create a Feature Branch**
3. **Commit Your Changes**
4. **Push to the Branch**
5. **Open a Pull Request**

### License

This project is licensed under the GPL-3.0 License. See the [LICENSE](LICENSE) file for details.


---

Happy querying!

