import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import groovy.io.FileType

class SqlGeneratorTask extends DefaultTask {
  def csvDir
  def defaultExt = '.csv'
	
	 
  @TaskAction
  def generateSql() {
    println "Generating Scripts from ${csvDir}"
    
		def dir = new File(csvDir)
		def sqlDir = new File("${csvDir}/sql").mkdir()
		def f = new File("${csvDir}/sql/create_database_script.sql")
 
    dir.eachFileMatch(~/.*?\.csv/){ file -> 
      
      //table name is file name
      def tableName = file.name - defaultExt
      
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
      f.append("\n);\n\n");
    }
  }
  
  def buildColumnScript(r){
    def str = ""
    r.tokenize(',').each{
      str += "${it}\t"
    }
    return str.trim()
  }
}