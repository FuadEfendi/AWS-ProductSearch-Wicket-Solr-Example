package ca.tokenizer.shareasale.model;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Product implements Serializable
{

    private static long serialVersionUID = 1L;

    private static Logger LOG = LoggerFactory.getLogger(Product.class);

    private static String COMMA = ",";

    private static String SPACE = " ";

    private static String AFFILIATE_ID = "234872";

    /**
     * ShareASale generated ID for use in conjunction with our online tools.
     * Should not be used as a primary key.
     */
    @Field("productId")
    private int productId;

    @Field("productName")
    private String productName;

    @Field("merchantId")
    private int merchantId;

    @Field("merchantName")
    private String merchantName;

    private URL linkToProduct;

    private URL linkToThumbnail;

    private URL linkToImage;

    private String price;

    /**
     * Product full retail price ( or MSRP)
     */
    private String retailPrice;

    /**
     * ShareASale defined Category
     */
    @Field("category")
    private String category;

    /**
     * ShareASale defined Subcategory
     */
    @Field("subcategory")
    private String subcategory;

    /**
     * Product Description ( HTML allowed)
     */
    @Field("description")
    private String description;

    /**
     * Merchant Defined Data
     */
    @Field("custom1")
    private String custom1;

    /**
     * Merchant Defined Data
     */
    @Field("custom2")
    private String custom2;

    /**
     * Merchant Defined Data
     */
    @Field("custom3")
    private String custom3;

    /**
     * Merchant Defined Data
     */
    @Field("custom4")
    private String custom4;

    /**
     * Merchant Defined Data
     */
    @Field("custom5")
    private String custom5;

    /**
     * Last updated date
     */
    @Field("lastupdated")
    private String lastupdated;

    /**
     * Stock Status. instock refers to product in stock
     */
    @Field("status")
    private String status;

    /**
     * Product Manufacturer
     */
    @Field("manufacturer")
    private String manufacturer;

    /**
     * Manufacture's part number
     */
    @Field("partNumber")
    private String partNumber;

    /**
     * Merchant defined category
     */
    @Field("merchantCategory")
    private String merchantCategory;

    /**
     * Merchant defined subcategory
     */
    @Field("merchantSubcategory")
    private String merchantSubcategory;

    /**
     * Short description ( no HTML)
     */
    @Field("shortDescription")
    private String shortDescription;

    /**
     * Product number
     */
    @Field("isbn")
    private String isbn;

    /**
     * Product UPC
     */
    @Field("upc")
    private String upc;

    /**
     * Merchant SKU for this product. This is a unique value, and can be used to
     * generate a primary key.
     */
    @Field("sku")
    private String sku;

    /**
     * Comma separated list of SKU values that cross sell with the product.
     */
    @Field("crossSell")
    private List<String> crossSell;

    /**
     * 3rd level category ( sub subcategory) for the product
     */
    @Field("merchantGroup")
    private String merchantGroup;

    /**
     * 4th level cateogy (sub sub subcategory) for the product.
     */
    @Field("merchantSubgroup")
    private String merchantSubgroup;

    /**
     * Comma separated list of compatible items in format of
     * Manufacturer~PartNumber.
     */
    @Field("compatibleWith")
    private List<String> compatibleWith;

    /**
     * Comma separated list of items this product can replace in format of
     * Manufacturer~PartNumber.
     */
    @Field("compareTo")
    private List<String> compareTo;

    /**
     * Comma separated list in the format of minQuantity~maxQuantity~itemCost.
     * Blank Max Quantity represents a top tier.
     */
    @Field("quantityDiscount")
    private List<String> quantityDiscount;

    /**
     * A 1 indicates a best selling product. Null values or zero are
     * non-bestsellers.
     */
    @Field("bestseller")
    private boolean bestseller;

    /**
     * URL that adds this product directly into the shopping cart.
     */
    private URL addToCartURL;

    /**
     * URL to RSS formatted reviews for this product.
     */
    private URL reviewsRSSURL;

    /**
     * Comma separated list of product options in the format
     * optionName~priceChangeInDollarsPerUnit.
     */
    @Field("option1")
    private List<String> option1;

    /**
     * Comma separated list of product options in the format
     * optionName~priceChangeInDollarsPerUnit.
     */
    @Field("option2")
    private List<String> option2;

    /**
     * Comma separated list of product options in the format
     * optionName~priceChangeInDollarsPerUnit.
     */
    @Field("option3")
    private List<String> option3;

    /**
     * Comma separated list of product options in the format
     * optionName~priceChangeInDollarsPerUnit.
     */
    @Field("option4")
    private List<String> option4;

    /**
     * Comma separated list of product options in the format
     * optionName~priceChangeInDollarsPerUnit.
     */
    @Field("option5")
    private List<String> option5;

    /**
     * URL to the product's page, optimized for mobile.
     */
    private URL mobileURL;

    /**
     * URL to the product's large image, optimized for mobile.
     */
    private URL mobileImage;

    /**
     * URL to the products thumbnail image, optimized for mobile.
     */
    private URL mobileThumbnail;

    public Product()
    {

    }

    /**
     * Constructor.
     * 
     * @param productId
     * @param productName
     * @param merchantId
     * @param merchantName
     * @param linkToProduct
     * @param linkToThumbnail
     * @param linkToImage
     * @param price
     * @param retailPrice
     * @param category
     * @param subcategory
     * @param description
     * @param custom1
     * @param custom2
     * @param custom3
     * @param custom4
     * @param custom5
     * @param lastupdated
     * @param status
     * @param manufacturer
     * @param partNumber
     * @param merchantCategory
     * @param merchantSubcategory
     * @param shortDescription
     * @param isbn
     * @param upc
     * @param sku
     * @param crossSell
     * @param merchantGroup
     * @param merchantSubgroup
     * @param compatibleWith
     * @param compareTo
     * @param quantityDiscount
     * @param bestseller
     * @param addToCartURL
     * @param reviewsRSSURL
     * @param option1
     * @param option2
     * @param option3
     * @param option4
     * @param option5
     * @param mobileURL
     * @param mobileImage
     * @param mobileThumbnail
     */
    public Product(
            final int productId,
            final String productName,
            final int merchantId,
            final String merchantName,
            final URL linkToProduct,
            final URL linkToThumbnail,
            final URL linkToImage,
            final String price,
            final String retailPrice,
            final String category,
            final String subcategory,
            final String description,
            final String custom1,
            final String custom2,
            final String custom3,
            final String custom4,
            final String custom5,
            final String lastupdated,
            final String status,
            final String manufacturer,
            final String partNumber,
            final String merchantCategory,
            final String merchantSubcategory,
            final String shortDescription,
            final String isbn,
            final String upc,
            final String sku,
            final List<String> crossSell,
            final String merchantGroup,
            final String merchantSubgroup,
            final List<String> compatibleWith,
            final List<String> compareTo,
            final List<String> quantityDiscount,
            final boolean bestseller,
            final URL addToCartURL,
            final URL reviewsRSSURL,
            final List<String> option1,
            final List<String> option2,
            final List<String> option3,
            final List<String> option4,
            final List<String> option5,
            final URL mobileURL,
            final URL mobileImage,
            final URL mobileThumbnail)
    {
        super();
        this.productId = productId;
        this.productName = productName;
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.linkToProduct = linkToProduct;
        this.linkToThumbnail = linkToThumbnail;
        this.linkToImage = linkToImage;
        this.price = price;
        this.retailPrice = retailPrice;
        this.category = category;
        this.subcategory = subcategory;
        this.description = description;
        this.custom1 = custom1;
        this.custom2 = custom2;
        this.custom3 = custom3;
        this.custom4 = custom4;
        this.custom5 = custom5;
        this.lastupdated = lastupdated;
        this.status = status;
        this.manufacturer = manufacturer;
        this.partNumber = partNumber;
        this.merchantCategory = merchantCategory;
        this.merchantSubcategory = merchantSubcategory;
        this.shortDescription = shortDescription;
        this.isbn = isbn;
        this.upc = upc;
        this.sku = sku;
        this.crossSell = crossSell;
        this.merchantGroup = merchantGroup;
        this.merchantSubgroup = merchantSubgroup;
        this.compatibleWith = compatibleWith;
        this.compareTo = compareTo;
        this.quantityDiscount = quantityDiscount;
        this.bestseller = bestseller;
        this.addToCartURL = addToCartURL;
        this.reviewsRSSURL = reviewsRSSURL;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.option5 = option5;
        this.mobileURL = mobileURL;
        this.mobileImage = mobileImage;
        this.mobileThumbnail = mobileThumbnail;
    }

    public Product(
            final String productId,
            final String productName,
            final String merchantId,
            final String merchantName,
            final String linkToProduct,
            final String linkToThumbnail,
            final String linkToImage,
            final String price,
            final String retailPrice,
            final String category,
            final String subcategory,
            final String description,
            final String custom1,
            final String custom2,
            final String custom3,
            final String custom4,
            final String custom5,
            final String lastupdated,
            final String status,
            final String manufacturer,
            final String partNumber,
            final String merchantCategory,
            final String merchantSubcategory,
            final String shortDescription,
            final String isbn,
            final String upc,
            final String sku,
            final String crossSell,
            final String merchantGroup,
            final String merchantSubgroup,
            final String compatibleWith,
            final String compareTo,
            final String quantityDiscount,
            final String bestseller,
            final String addToCartURL,
            final String reviewsRSSURL,
            final String option1,
            final String option2,
            final String option3,
            final String option4,
            final String option5,
            final String mobileURL,
            final String mobileImage,
            final String mobileThumbnail)
    {
        super();
        this.productId = Integer.parseInt(productId);
        this.productName = productName;
        this.merchantId = Integer.parseInt(merchantId);
        this.merchantName = merchantName;
        this.linkToProduct = parseUrl(linkToProduct, AFFILIATE_ID);
        this.linkToThumbnail = parseUrl(linkToThumbnail, AFFILIATE_ID);
        this.linkToImage = parseUrl(linkToImage, AFFILIATE_ID);
        this.price = price;
        this.retailPrice = retailPrice;
        this.category = category;
        this.subcategory = subcategory;
        this.description = description;
        this.custom1 = custom1;
        this.custom2 = custom2;
        this.custom3 = custom3;
        this.custom4 = custom4;
        this.custom5 = custom5;
        this.lastupdated = lastupdated;
        this.status = status;
        this.manufacturer = manufacturer;
        this.partNumber = partNumber;
        this.merchantCategory = merchantCategory;
        this.merchantSubcategory = merchantSubcategory;
        this.shortDescription = shortDescription;
        this.isbn = isbn;
        this.upc = upc;
        this.sku = sku;

        this.crossSell = tokenize(crossSell);
        this.merchantGroup = merchantGroup;
        this.merchantSubgroup = merchantSubgroup;

        this.compatibleWith = tokenize(compatibleWith);
        this.compareTo = tokenize(compareTo);

        this.quantityDiscount = tokenize(quantityDiscount);

        this.bestseller = "1".equals(bestseller);
        this.addToCartURL = parseUrl(addToCartURL, AFFILIATE_ID);
        this.reviewsRSSURL = parseUrl(reviewsRSSURL, AFFILIATE_ID);
        this.option1 = tokenize(option1);
        this.option2 = tokenize(option2);
        this.option3 = tokenize(option3);
        this.option4 = tokenize(option4);
        this.option5 = tokenize(option5);
        this.mobileURL = parseUrl(mobileURL, AFFILIATE_ID);
        this.mobileImage = parseUrl(mobileImage, AFFILIATE_ID);
        this.mobileThumbnail = parseUrl(mobileThumbnail, AFFILIATE_ID);
    }

    public int getProductId()
    {
        return productId;
    }

    public String getProductName()
    {
        return productName;
    }

    public int getMerchantId()
    {
        return merchantId;
    }

    public String getMerchantName()
    {
        return merchantName;
    }

    public URL getLinkToProduct()
    {
        return linkToProduct;
    }

    public URL getLinkToThumbnail()
    {
        return linkToThumbnail;
    }

    public URL getLinkToImage()
    {
        return linkToImage;
    }

    public String getPrice()
    {
        return price;
    }

    public String getRetailPrice()
    {
        return retailPrice;
    }

    public String getCategory()
    {
        return category;
    }

    public String getSubcategory()
    {
        return subcategory;
    }

    public String getDescription()
    {
        return description;
    }

    public String getCustom1()
    {
        return custom1;
    }

    public String getCustom2()
    {
        return custom2;
    }

    public String getCustom3()
    {
        return custom3;
    }

    public String getCustom4()
    {
        return custom4;
    }

    public String getCustom5()
    {
        return custom5;
    }

    public String getLastupdated()
    {
        return lastupdated;
    }

    public String getStatus()
    {
        return status;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }

    public String getPartNumber()
    {
        return partNumber;
    }

    public String getMerchantCategory()
    {
        return merchantCategory;
    }

    public String getMerchantSubcategory()
    {
        return merchantSubcategory;
    }

    public String getShortDescription()
    {
        return shortDescription;
    }

    public String getIsbn()
    {
        return isbn;
    }

    public String getUpc()
    {
        return upc;
    }

    public String getSku()
    {
        return sku;
    }

    public List<String> getCrossSell()
    {
        return crossSell;
    }

    public String getMerchantGroup()
    {
        return merchantGroup;
    }

    public String getMerchantSubgroup()
    {
        return merchantSubgroup;
    }

    public List<String> getCompatibleWith()
    {
        return compatibleWith;
    }

    public List<String> getCompareTo()
    {
        return compareTo;
    }

    public List<String> getQuantityDiscount()
    {
        return quantityDiscount;
    }

    public boolean isBestseller()
    {
        return bestseller;
    }

    public URL getAddToCartURL()
    {
        return addToCartURL;
    }

    public URL getReviewsRSSURL()
    {
        return reviewsRSSURL;
    }

    public List<String> getOption1()
    {
        return option1;
    }

    public List<String> getOption2()
    {
        return option2;
    }

    public List<String> getOption3()
    {
        return option3;
    }

    public List<String> getOption4()
    {
        return option4;
    }

    public List<String> getOption5()
    {
        return option5;
    }

    public URL getMobileURL()
    {
        return mobileURL;
    }

    public URL getMobileImage()
    {
        return mobileImage;
    }

    public URL getMobileThumbnail()
    {
        return mobileThumbnail;
    }

    private static List<String> tokenize(final String v)
    {

        List<String> ret;
        final StringTokenizer st = new StringTokenizer(v, COMMA);
        if (st.hasMoreTokens())
        {
            ret = new ArrayList<>();
            while (st.hasMoreElements())
            {
                ret.add(st.nextToken());
            }
        }
        else
        {
            ret = null;
        }
        return ret;
    }

    private static URL parseUrl(final String p_url, final String p_merchantId)
    {

        if (p_url == null)
        {
            return null;
        }

        if (p_url.isEmpty())
        {
            return null;
        }

        final String temp = p_url.replaceAll("YOURUSERID", p_merchantId);

        try
        {
            return new URL(temp);
        }
        catch (final MalformedURLException e)
        {
            LOG.error(e.getMessage() + "URL: " + p_url);
            return null;
        }

    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append("Product [productId=");
        builder.append(productId);
        builder.append(", productName=");
        builder.append(productName);
        builder.append(", merchantId=");
        builder.append(merchantId);
        builder.append(", merchantName=");
        builder.append(merchantName);
        builder.append(", linkToProduct=");
        builder.append(linkToProduct);
        builder.append(", linkToThumbnail=");
        builder.append(linkToThumbnail);
        builder.append(", linkToImage=");
        builder.append(linkToImage);
        builder.append(", price=");
        builder.append(price);
        builder.append(", retailPrice=");
        builder.append(retailPrice);
        builder.append(", category=");
        builder.append(category);
        builder.append(", subcategory=");
        builder.append(subcategory);
        builder.append(", description=");
        builder.append(description);
        builder.append(", custom1=");
        builder.append(custom1);
        builder.append(", custom2=");
        builder.append(custom2);
        builder.append(", custom3=");
        builder.append(custom3);
        builder.append(", custom4=");
        builder.append(custom4);
        builder.append(", custom5=");
        builder.append(custom5);
        builder.append(", lastupdated=");
        builder.append(lastupdated);
        builder.append(", status=");
        builder.append(status);
        builder.append(", manufacturer=");
        builder.append(manufacturer);
        builder.append(", partNumber=");
        builder.append(partNumber);
        builder.append(", merchantCategory=");
        builder.append(merchantCategory);
        builder.append(", merchantSubcategory=");
        builder.append(merchantSubcategory);
        builder.append(", shortDescription=");
        builder.append(shortDescription);
        builder.append(", isbn=");
        builder.append(isbn);
        builder.append(", upc=");
        builder.append(upc);
        builder.append(", sku=");
        builder.append(sku);
        builder.append(", crossSell=");
        builder.append(crossSell);
        builder.append(", merchantGroup=");
        builder.append(merchantGroup);
        builder.append(", merchantSubgroup=");
        builder.append(merchantSubgroup);
        builder.append(", compatibleWith=");
        builder.append(compatibleWith);
        builder.append(", compareTo=");
        builder.append(compareTo);
        builder.append(", quantityDiscount=");
        builder.append(quantityDiscount);
        builder.append(", bestseller=");
        builder.append(bestseller);
        builder.append(", addToCartURL=");
        builder.append(addToCartURL);
        builder.append(", reviewsRSSURL=");
        builder.append(reviewsRSSURL);
        builder.append(", option1=");
        builder.append(option1);
        builder.append(", option2=");
        builder.append(option2);
        builder.append(", option3=");
        builder.append(option3);
        builder.append(", option4=");
        builder.append(option4);
        builder.append(", option5=");
        builder.append(option5);
        builder.append(", mobileURL=");
        builder.append(mobileURL);
        builder.append(", mobileImage=");
        builder.append(mobileImage);
        builder.append(", mobileThumbnail=");
        builder.append(mobileThumbnail);
        builder.append("]");
        return builder.toString();
    }

    public SolrInputDocument toSolr()
    {
        final SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", merchantId + SPACE + isbn + SPACE + upc + SPACE + sku);
        doc.addField("productId", productId);
        doc.addField("productName", productName);
        doc.addField("merchantId", merchantId);
        doc.addField("merchantName", merchantName);
        doc.addField("linkToProduct", linkToProduct);
        doc.addField("linkToThumbnail", linkToThumbnail);
        doc.addField("linkToImage", linkToImage);
        doc.addField("price", price);
        doc.addField("retailPrice", retailPrice);
        doc.addField("category", category);
        doc.addField("subcategory", subcategory);
        doc.addField("description", description);
        doc.addField("custom1", custom1);
        doc.addField("custom2", custom2);
        doc.addField("custom3", custom3);
        doc.addField("custom4", custom4);
        doc.addField("custom5", custom5);
        doc.addField("lastupdated", lastupdated);
        doc.addField("status", status);
        doc.addField("manufacturer", manufacturer);
        doc.addField("partNumber", partNumber);
        doc.addField("merchantCategory", merchantCategory);
        doc.addField("merchantSubcategory", merchantSubcategory);
        doc.addField("shortDescription", shortDescription);
        doc.addField("isbn", isbn);
        doc.addField("upc", upc);
        doc.addField("sku", sku);
        doc.addField("crossSell", crossSell);
        doc.addField("merchantGroup", merchantGroup);
        doc.addField("merchantSubgroup", merchantSubgroup);
        doc.addField("compatibleWith", compatibleWith);
        doc.addField("compareTo", compareTo);
        doc.addField("quantityDiscount", quantityDiscount);
        doc.addField("bestseller", bestseller);
        doc.addField("addToCartURL", addToCartURL);
        doc.addField("reviewsRSSURL", reviewsRSSURL);
        doc.addField("option1", option1);
        doc.addField("option2", option2);
        doc.addField("option3", option3);
        doc.addField("option4", option4);
        doc.addField("option5", option5);
        doc.addField("mobileURL", mobileURL);
        doc.addField("mobileImage", mobileImage);
        doc.addField("mobileThumbnail", mobileThumbnail);
        return doc;
    }

    /**
     * Setter method for linkToProduct
     * 
     * @param linkToProduct
     *            the linkToProduct to set
     */
    public void setLinkToProduct(final URL linkToProduct)
    {
        this.linkToProduct = linkToProduct;
    }

    /**
     * Setter method for linkToThumbnail
     * 
     * @param linkToThumbnail
     *            the linkToThumbnail to set
     */
    public void setLinkToThumbnail(final URL linkToThumbnail)
    {
        this.linkToThumbnail = linkToThumbnail;
    }

    /**
     * Setter method for linkToImage
     * 
     * @param linkToImage
     *            the linkToImage to set
     */
    public void setLinkToImage(final URL linkToImage)
    {
        this.linkToImage = linkToImage;
    }

    /**
     * Setter method for addToCartURL
     * 
     * @param addToCartURL
     *            the addToCartURL to set
     */
    public void setAddToCartURL(final URL addToCartURL)
    {
        this.addToCartURL = addToCartURL;
    }

    /**
     * Setter method for reviewsRSSURL
     * 
     * @param reviewsRSSURL
     *            the reviewsRSSURL to set
     */
    public void setReviewsRSSURL(final URL reviewsRSSURL)
    {
        this.reviewsRSSURL = reviewsRSSURL;
    }

    /**
     * Setter method for mobileURL
     * 
     * @param mobileURL
     *            the mobileURL to set
     */
    public void setMobileURL(final URL mobileURL)
    {
        this.mobileURL = mobileURL;
    }

    /**
     * Setter method for mobileImage
     * 
     * @param mobileImage
     *            the mobileImage to set
     */
    public void setMobileImage(final URL mobileImage)
    {
        this.mobileImage = mobileImage;
    }

    /**
     * Setter method for mobileThumbnail
     * 
     * @param mobileThumbnail
     *            the mobileThumbnail to set
     */
    public void setMobileThumbnail(final URL mobileThumbnail)
    {
        this.mobileThumbnail = mobileThumbnail;
    }

    /**
     * Setter method for linkToProduct
     * 
     * @param linkToProduct
     *            the linkToProduct to set
     */
    @Field
    public void setLinkToProduct(final String linkToProduct)
    {
        this.linkToProduct = parseUrl(linkToProduct, AFFILIATE_ID);
    }

    /**
     * Setter method for linkToThumbnail
     * 
     * @param linkToThumbnail
     *            the linkToThumbnail to set
     */
    @Field
    public void setLinkToThumbnail(final String linkToThumbnail)
    {
        this.linkToThumbnail = parseUrl(linkToThumbnail, AFFILIATE_ID);
    }

    /**
     * Setter method for linkToImage
     * 
     * @param linkToImage
     *            the linkToImage to set
     */
    @Field
    public void setLinkToImage(final String linkToImage)
    {
        this.linkToImage = parseUrl(linkToImage, AFFILIATE_ID);
    }

    /**
     * Setter method for addToCartURL
     * 
     * @param addToCartURL
     *            the addToCartURL to set
     */
    @Field
    public void setAddToCartURL(final String addToCartURL)
    {
        this.addToCartURL = parseUrl(addToCartURL, AFFILIATE_ID);
    }

    /**
     * Setter method for reviewsRSSURL
     * 
     * @param reviewsRSSURL
     *            the reviewsRSSURL to set
     */
    @Field
    public void setReviewsRSSURL(final String reviewsRSSURL)
    {
        this.reviewsRSSURL = parseUrl(reviewsRSSURL, AFFILIATE_ID);
    }

    /**
     * Setter method for mobileURL
     * 
     * @param mobileURL
     *            the mobileURL to set
     */
    @Field
    public void setMobileURL(final String mobileURL)
    {
        this.mobileURL = parseUrl(mobileURL, AFFILIATE_ID);
    }

    /**
     * Setter method for mobileImage
     * 
     * @param mobileImage
     *            the mobileImage to set
     */
    @Field
    public void setMobileImage(final String mobileImage)
    {
        this.mobileImage = parseUrl(mobileImage, AFFILIATE_ID);
    }

    /**
     * Setter method for mobileThumbnail
     * 
     * @param mobileThumbnail
     *            the mobileThumbnail to set
     */
    @Field
    public void setMobileThumbnail(final String mobileThumbnail)
    {
        this.mobileThumbnail = parseUrl(mobileThumbnail, AFFILIATE_ID);
    }

    /**
     * Setter method for price
     * 
     * @param price
     *            the price to set
     */
    @Field
    public void setPrice(final String price)
    {
        this.price = price;
    }

    /**
     * Setter method for retailPrice
     * 
     * @param retailPrice
     *            the retailPrice to set
     */
    @Field
    public void setRetailPrice(final String retailPrice)
    {
        this.retailPrice = retailPrice;
    }

}
