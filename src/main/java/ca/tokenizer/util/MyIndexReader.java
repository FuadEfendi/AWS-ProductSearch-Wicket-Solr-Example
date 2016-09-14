package ca.tokenizer.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

/** Simple command-line based search demo. */
public class MyIndexReader
{

    private MyIndexReader()
    {
    }

    /** Simple command-line based search demo. */
    public static void main(final String[] args) throws Exception
    {

        final IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("C:/data/solr/21462/index")));

        final Terms terms = SlowCompositeReaderWrapper.wrap(reader).terms("s1");

        final TermsEnum termsEnum = terms.iterator(null);
        BytesRef term = null;
        while ((term = termsEnum.next()) != null)
        {
            if (termsEnum.totalTermFreq() < 10)
            {
                System.out.println(term.utf8ToString()); // + " :: " + termsEnum.totalTermFreq());
            }

        }

        reader.close();
    }

}
