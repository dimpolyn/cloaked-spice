import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class SqliteTask extends DefaultTask {
  def sql 
  
  @TaskAction
  def runQuery() {
    println "hello ${sql}"
  }
}