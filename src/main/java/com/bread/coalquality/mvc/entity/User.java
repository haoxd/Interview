package com.bread.coalquality.mvc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
@ToString
@TableName("tb_user")
public class User extends Model<User>{


    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @NotBlank(message = "名字不可为空")
    @Length(min = 2 ,max = 6 ,message = "名字为2-6位")
    @TableField(value = "username")
    private String userName;

    @NotBlank(message = "密码不可为空")
    @Length(min = 8 ,max = 14 ,message = "密码位8-14位")
    @TableField(value = "password")
    private String passWord;
}
