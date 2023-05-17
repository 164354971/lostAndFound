package love.lingbao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LosePage {
    private Integer page;
    private Integer pageSize;
    private String name;
}
