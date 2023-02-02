package com.example.jpastudy.entity;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="cart")
@Getter
@Setter
@ToString
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="cart_id")
  private Long id;

  @OneToOne
  @JoinColumn(name = "member_id")
  private Member member;
}
