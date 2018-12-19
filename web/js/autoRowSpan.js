// The javaScript is used to auto unite the rowspan

  function autoRowSpan(tb,row,col)
    {
        var lastValue="";
        var value="";
        var pos=1;
        for(var i=row;i<tb.rows.length;i++)
        {
            value = tb.rows[i].cells[col].innerText;
            if(lastValue == value)
            {
                tb.rows[i].deleteCell(col);
                tb.rows[i-pos].cells[col].rowSpan = tb.rows[i-pos].cells[col].rowSpan+1;
                pos++;
            }else{
                lastValue = value;
                pos=1;
            }
        }
    }
