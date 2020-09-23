# findBrokenGlass

## About
This repo is a open source tool to check a connection status of URLs found from the input file, using Java. It returns "[code] url status" in each line.


## Getting Started
 
  1. Clone the repo
  
  ```bash
  git clone https://github.com/eunbeek/findBrokenGlass.git
  ```
  
  2. Run the UrlCheck.exe file in bin folder on CMD
  <p align="Left">
  <img src="./asset/defaultTool.png" alt="DefaultPic" width="738">
  </p>
  
  
## Usage
  
  This command returns a help message for the flag and the argument explanation.
  ```bash
  UrlCheck help
  ```
  
  You can type the input file name after tool name, then return the response code, URL and status in each line.
  This tool accepts multiple files and delimiters by space.
  ```bash
  UrlCheck <fileName>
  ```

  ```bash
  UrlCheck <fileName1> <fileName2>
  ```
  
  You can type '-a' flag to allow checking for archived versions of URLs.
  ```bash
  UrlCheck -a <fileName>
  ```
   
  This '-s' flag change 'http' to 'https' in URLs.  
  ```bash
  UrlCheck -s <fileName>
  ```

## Library 
https://github.com/java-native-access/jna
  
  


