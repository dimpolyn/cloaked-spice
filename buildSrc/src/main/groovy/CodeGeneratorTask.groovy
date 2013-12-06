import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import groovy.io.FileType

class CodeGeneratorTask extends DefaultTask {
  def srcDir
  def sqlDir
  def defaultExt = '.csv'
  
  @TaskAction
  def generateSql() {
    println "Generating Scripts from ${srcDir}"
    println "Placing them in ${sqlDir}"
    
    def dir = new File(srcDir)
    dir.eachFileMatch(~/.*?\.csv/){ file -> 
      
      //table name is file name
      def tableName = file.name - defaultExt
      
      File f = new File("${srcDir}\\${tableName}.sql")
      
      //start writing the script
      f.write("--SQL Script for table ${tableName}\n")
      f.write("CREATE TABLE ${tableName} (\n\t")
      
      def rows = file.readLines()
      
      //remove first header of csv file
      rows.remove(0)
      def lastRow = rows.remove(rows.size()-1)
      
      rows.each{
        f.append("${buildColumnScript(it)},\n\t")
      }
      
      f.append(buildColumnScript(lastRow))
      f.append(");");
    }
  }
  
  def buildColumnScript(r){
    def str = ""
    r.tokenize(',').each{
      str += "${it}\t"
    }
    return str
  }
}