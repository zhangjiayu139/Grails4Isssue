package middol.excel

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.grails.plugins.excelimport.AbstractExcelImporter
import org.grails.plugins.excelimport.ExcelImportService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
* @Description:    demo导入数据
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/16 15:00
* @Version:        1.0
*/
@Component
class DemoImport {
    @Autowired
    ExcelImportService excelImportService
    //表头
    static Map CONFIG_BOOK_COLUMN_MAP = [
            sheet:'Sheet1',
            startRow: 1,
            columnMap:  [
                    //Col, Map-Key
                    'A':'demo1',
                    'B':'demo2',
                    'C':'demo3',
                    'D':'demo4',
                    'E':'demo5',
                    'F':'demo6',
                    'G':'demo7'
            ]
    ]

    List<Map> getData(InputStream inputStream) {
        //将文件放到输入流中
        Workbook workbook = WorkbookFactory.create(inputStream)

        //解析Excel的行存入list
        List list = excelImportService.columns(workbook,CONFIG_BOOK_COLUMN_MAP)
        return list
    }
}
