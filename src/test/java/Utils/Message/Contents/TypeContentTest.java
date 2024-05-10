package Utils.Message.Contents;
import org.junit.jupiter.api.Test;
import Utils.Message.EnumTypes.ContentSubtype;
import Utils.Message.EnumTypes.ContentTypes;

import static org.junit.jupiter.api.Assertions.*;

public class TypeContentTest {

    @Test
    public void testGetByteMessage() {
        ContentSubtype subtype = new ContentSubtype() {
            @Override
            public ContentTypes getSuperType() {
                return ContentTypes.ERROR;
            }
        };
        TypeContent content = new TypeContent(subtype);
        assertEquals(ContentTypes.ERROR.toString().getBytes(), content.getByteMessage());
    }

    @Test
    public void testGetStringMessage() {
        ContentSubtype subtype = new ContentSubtype() {
            @Override
            public ContentTypes getSuperType() {
                return ContentTypes.ERROR;
            }
        };
        TypeContent content = new TypeContent(subtype);
        assertEquals(ContentTypes.ERROR.toString(), content.getStringMessage());
    }

    @Test
    public void testGetType() {
        ContentSubtype subtype = new ContentSubtype() {
            @Override
            public ContentTypes getSuperType() {
                return ContentTypes.ERROR;
            }
        };
        TypeContent content = new TypeContent(subtype);
        assertEquals(ContentTypes.ERROR, content.getType());
    }

    @Test
    public void testGetSubType() {
        ContentSubtype subtype = new ContentSubtype() {
            @Override
            public ContentTypes getSuperType() {
                return ContentTypes.ERROR;
            }
        };
        TypeContent content = new TypeContent(subtype);
        assertEquals(subtype, content.getSubType());
    }
}

