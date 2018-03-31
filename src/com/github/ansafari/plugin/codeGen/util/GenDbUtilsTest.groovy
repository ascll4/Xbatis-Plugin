package com.github.ansafari.plugin.codeGen.util

import com.github.ansafari.plugin.codeGen.model.DataSource
import com.github.ansafari.plugin.codeGen.model.gen.GenTable


/**
 * Created with IntelliJ IDEA. 
 * User: xiongjinteng@raycloud.com
 * Date: 2017/4/21
 * Time: 上午8:18
 */
class GenDbUtilsTest extends GroovyTestCase {
    private DataSource dataSource;

    void setUp() {
        super.setUp()
        dataSource = GenDbUtils.getDefaultDataSource();
    }

    void testGetConntion() {
    }

    void testGetTableList() {
        List<GenTable> genTableList = GenDbUtils.getGenTableList(dataSource);
        for (GenTable genTable : genTableList) {
            println genTable.getName() + " " + genTable.getComments()
        }
    }

    void testGetGenTableList() {

    }

    void testGetColumnList() {

    }

    void testGetColumnListBySql() {

    }

    void testGetDefaultDataSource() {

    }

    void testMain() {

    }
}
