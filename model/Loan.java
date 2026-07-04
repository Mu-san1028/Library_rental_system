package model;
import java.time.LocalDate;

public class Loan {

    private final Member member;
    private final Book   book;
    private final LocalDate loanDate;

    public Loan(Member member, Book book) {
        if (member == null) throw new IllegalArgumentException("Member cannot be null.");
        if (book   == null) throw new IllegalArgumentException("Book cannot be null.");
        this.member = member;
        this.loanDate = LocalDate.now();
        this.book   = book;
    }

    public Member getMember() { return member; }
    public Book   getBook()   { return book; }
    public LocalDate getLoanDate() { return loanDate; }

    @Override
    public String toString() {
        return String.format("Loan{memberId=%d, memberName='%s', bookTitle='%s', loanDate=%s}",
                member.getMemberId(), member.getName(), book.getTitle(), loanDate);
    }

    public boolean isForMember(Member member, Book book) {
        return this.member.equals(member) && this.book.equals(book);
    }
}
