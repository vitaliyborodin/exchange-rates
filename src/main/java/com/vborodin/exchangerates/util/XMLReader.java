package com.vborodin.exchangerates.util;

import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.model.ExchangeRateId;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class XMLReader implements Reader {

    private MultipartFile file;

    public XMLReader(MultipartFile file) {
        this.file = file;
    }

    @Override
    public List<ExchangeRate> read() {
        List<ExchangeRate> exchangeRateList = new ArrayList<>();

        try {
            InputStream is = file.getInputStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();
            NodeList nList = root.getElementsByTagName("exchangeRate");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String currency = eElement.getElementsByTagName("currency")
                            .item(0)
                            .getTextContent();
                    String buy = eElement.getElementsByTagName("buy")
                            .item(0)
                            .getTextContent();
                    String sell = eElement.getElementsByTagName("sell")
                            .item(0)
                            .getTextContent();

                    ExchangeRate rateObj = new ExchangeRate(
                            new ExchangeRateId(currency, FilenameUtils.getBaseName(file.getOriginalFilename())),
                            new BigDecimal(buy),
                            new BigDecimal(sell)
                    );
                    exchangeRateList.add(rateObj);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return exchangeRateList;
    }
}
